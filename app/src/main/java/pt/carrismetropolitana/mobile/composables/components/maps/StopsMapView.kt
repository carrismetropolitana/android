package pt.carrismetropolitana.mobile.composables.components.maps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.location.LocationComponentActivationOptions
import org.maplibre.android.location.LocationComponentOptions
import org.maplibre.android.location.engine.LocationEngineRequest
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.style.expressions.Expression.interpolate
import org.maplibre.android.style.expressions.Expression.linear
import org.maplibre.android.style.expressions.Expression.stop
import org.maplibre.android.style.expressions.Expression.zoom
import org.maplibre.android.style.layers.CircleLayer
import org.maplibre.android.style.layers.Property
import org.maplibre.android.style.layers.PropertyFactory.circleColor
import org.maplibre.android.style.layers.PropertyFactory.circlePitchAlignment
import org.maplibre.android.style.layers.PropertyFactory.circleRadius
import org.maplibre.android.style.layers.PropertyFactory.circleStrokeColor
import org.maplibre.android.style.layers.PropertyFactory.circleStrokeWidth
import org.maplibre.android.style.layers.RasterLayer
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.android.style.sources.RasterSource
import org.maplibre.android.style.sources.TileSet
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.Point
import pt.carrismetropolitana.mobile.helpers.checkLocationPermission
import pt.carrismetropolitana.mobile.services.cmapi.Stop

enum class MapVisualStyle {
    MAP, SATELLITE
}

@Composable
fun StopsMapView(
    modifier: Modifier = Modifier,
    stops: List<Stop>,
    mapVisualStyle: MapVisualStyle = MapVisualStyle.MAP,
    onMapReady: (MapLibreMap) -> Unit = {},
    onStopClick: (stopId: String) -> Unit = {}
) {
    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle(context)

    AndroidView(
        modifier = modifier,
        factory = {
            mapView.apply {
                getMapAsync { map ->
                    map.setStyle("https://maps.carrismetropolitana.pt/styles/default/style.json") { style ->

//                        enableLocationComponent(map, style, context)

                        // Hide attributions
                        map.uiSettings.isAttributionEnabled = false
                        map.uiSettings.isLogoEnabled = false
                        // Set the map view center
                        map.cameraPosition = CameraPosition.Builder()
                            .target(LatLng(38.7, -9.0))
                            .zoom(8.9)
                            .build()

                        setVisualStyle(mapVisualStyle, style)

                        // Add stops source
                        val geoJsonSource = GeoJsonSource("stops-source", createGeoJsonFromStops(stops))
                        style.addSource(geoJsonSource)

                        // Add stops layer
                        val symbolLayer = CircleLayer("stops-layer", "stops-source")
                            .withProperties(
                                circleColor("#ffdd01"),
                                circleStrokeColor("#000000"),
                                circleRadius(
                                    interpolate(
                                        linear(), zoom(),
                                        stop(9, 1f),
                                        stop(26, 20f)
                                    )
                                ),
                                circleStrokeWidth(
                                    interpolate(
                                        linear(), zoom(),
                                        stop(9, 0.01f),
                                        stop(26, 7f)
                                    )
                                ),
                                circlePitchAlignment(Property.CIRCLE_PITCH_ALIGNMENT_MAP)
                            )

                        style.addLayer(symbolLayer)

                        // Setup map click listener to handle stop clicks
                        map.addOnMapClickListener { point ->
                            val screenPoint = map.projection.toScreenLocation(point)
                            val features = map.queryRenderedFeatures(screenPoint, "stops-layer")

                            if (features.isNotEmpty()) {
                                val feature = features[0]
                                val stopId = feature.getStringProperty("id")
                                onStopClick(stopId)
                                true
                            } else {
                                false
                            }
                        }

                        onMapReady(map)
                    }
                }
            }
        },
        update = {
            it.getMapAsync { mapboxMap ->
                mapboxMap.getStyle { style ->
                    val geoJsonSource = style.getSourceAs<GeoJsonSource>("stops-source")
                    geoJsonSource?.setGeoJson(createGeoJsonFromStops(stops))

                    setVisualStyle(mapVisualStyle, style)
                }
            }
        }
    )
}

@Composable
fun rememberMapViewWithLifecycle(context: Context): MapView {
    MapLibre.getInstance(context)
    val mapView = remember { MapView(context) }
    DisposableEffect(mapView) {
        mapView.onStart()
        mapView.onResume()
        onDispose {
            mapView.onPause()
            mapView.onStop()
            mapView.onDestroy()
        }
    }
    return mapView
}


fun createGeoJsonFromStops(stops: List<Stop>): FeatureCollection {
    println("Creating GeoJson from ${stops.size} stops")
    val features = stops.map { stop ->
        val point = Point.fromLngLat(stop.lon.toDouble(), stop.lat.toDouble())
        Feature.fromGeometry(point).apply {
            addStringProperty("id", stop.stopId)
            addStringProperty("name", stop.name)
        }
    }
    return FeatureCollection.fromFeatures(features)
}

fun setVisualStyle(mapVisualStyle: MapVisualStyle, style: Style) {
    if (mapVisualStyle == MapVisualStyle.MAP) {
        if (style.getLayer("satellite-layer") != null) {
            style.removeLayer("satellite-layer")
        }
    } else {
        val tileSet = TileSet(
            "tileset",
            "https://server.arcgisonline.com/arcgis/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}"
        )

        tileSet.minZoom = 5f
        tileSet.maxZoom = 18f
        tileSet.attribution = "Esri, Maxar, Earthstar Geographics, and the GIS User Community"

        val satelliteTilesSource = RasterSource(
            id = "satellite-source",
            tileSet = tileSet,
            tileSize = 256
        )

        if (style.getSource("satellite-source") == null) style.addSource(satelliteTilesSource)

        val satelliteLayer = RasterLayer("satellite-layer", "satellite-source")

        style.addLayerBelow(satelliteLayer, "stops-layer")
    }
}

fun enableLocationComponent(map: MapLibreMap, loadedMapStyle: Style, context: Context) {
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        // Create and customize the LocationComponent's options
        val locationComponentOptions = LocationComponentOptions.builder(context)
            .pulseEnabled(true)
            .build()

        val locationComponent = map.locationComponent

        // Activate with a built LocationComponentActivationOptions object
        locationComponent.activateLocationComponent(
            LocationComponentActivationOptions.builder(context, loadedMapStyle)
                .locationComponentOptions(locationComponentOptions)
                .useDefaultLocationEngine(true)
                .locationEngineRequest(
                    LocationEngineRequest.Builder(750)
                        .setFastestInterval(750)
                        .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                        .build()
                )
                .build()
        )

        // Enable to make component visible
        locationComponent.isLocationComponentEnabled = true
    }
}
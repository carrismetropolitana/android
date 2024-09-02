package pt.carrismetropolitana.mobile.composables.components.maps

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.style.expressions.Expression.interpolate
import org.maplibre.android.style.expressions.Expression.linear
import org.maplibre.android.style.expressions.Expression.stop
import org.maplibre.android.style.expressions.Expression.zoom
import org.maplibre.android.style.layers.CircleLayer
import org.maplibre.android.style.layers.Property
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.layers.PropertyFactory.circleColor
import org.maplibre.android.style.layers.PropertyFactory.circlePitchAlignment
import org.maplibre.android.style.layers.PropertyFactory.circleRadius
import org.maplibre.android.style.layers.PropertyFactory.circleStrokeColor
import org.maplibre.android.style.layers.PropertyFactory.circleStrokeWidth
import org.maplibre.android.style.layers.SymbolLayer
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.Point
import pt.carrismetropolitana.mobile.services.cmapi.Stop

@Composable
fun StopsMapView(
    modifier: Modifier = Modifier,
    stops: List<Stop>,
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
                        // Hide attributions
                        map.uiSettings.isAttributionEnabled = false
                        map.uiSettings.isLogoEnabled = false
                        // Set the map view center
                        map.cameraPosition = CameraPosition.Builder()
                            .target(LatLng(38.7, -9.0))
                            .zoom(8.9)
                            .build()

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
                }
            }
        }
    )
}

@Composable
fun rememberMapViewWithLifecycle(context: Context): MapView {
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
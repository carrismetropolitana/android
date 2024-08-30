package pt.carrismetropolitana.mobile.composables.screens.stops

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.carrismetropolitana.mobile.MLNMapView
import pt.carrismetropolitana.mobile.R
import pt.carrismetropolitana.mobile.composables.components.maps.MapFloatingButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StopsScreen(navController: NavController) {
    var text by rememberSaveable {
        mutableStateOf("")
    }

    var active by rememberSaveable {
        mutableStateOf(false)
    }

//    LaunchedEffect(Unit) {
//        coroutineScope {
//            launch {
//                val response = cmApi.getStops()
//                if (response.isSuccessful) {
//                    println(response.body())
//                }
//            }
//        }
//    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        MapLibre(
//            modifier = Modifier.fillMaxSize(),
//            styleUrl = "https://maps.carrismetropolitana.pt/styles/default/style.json",
//            cameraPosition = CameraPosition(target = LatLng(38.7, -9.0), zoom = 8.9)
//        )
//        AndroidView(
//            factory = { context ->
//                MapLibreMapView(context).apply {
//                    layoutParams = ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT
//                    )
//                }
//            },
//            update = { view ->
//                // update view if needed
//            }
//        )
        Box(modifier = Modifier.fillMaxSize()) {
            MLNMapView() // TODO: this is getting initalized everytime the StopsScreen is opened, keep state between screen changes

            SearchBar(
                query = text,
                onQueryChange = {
                    text = it
                },
                onSearch = {
                    active = false
                },
                active = active,
                onActiveChange = {
                    active = it
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                },
                trailingIcon = {
                    if (active)
                        Icon(
                            modifier = Modifier.clickable {
                                if (text.isNotEmpty()) {
                                    text = ""
                                } else {
                                    active = false
                                }
                            },
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Icon"
                        )
                },
                placeholder = {
                    Text(text = "Pesquisar paragens")
                },
                modifier = Modifier
                    .align(Alignment.TopCenter)
            ) {
                Row {
                    Column {
                        Text("Alameda Edgar Cardoso")
                        Text(text = "Lisboa")
                    }
                }
            }

//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(bottom = 150.dp),
//                verticalArrangement = Arrangement.Bottom,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Button(onClick = { navController.navigate("stop_details") })  {
//                    Text("Ver detalhes da paragem de demonstração")
//                }
//            }
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 12.dp)
                    .padding(bottom = 130.dp), // TODO: use paddingValues to account for bottom safe area
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    MapFloatingButton(iconResourceId = R.drawable.phosphoricons_navigation_arrow_fill) {

                    }
                    MapFloatingButton(iconResourceId = R.drawable.phosphoricons_map_trifold) {

                    }
                }
            }
        }
    }
}

package pt.carrismetropolitana.mobile

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import pt.carrismetropolitana.mobile.ui.theme.CarrisMetropolitanaTheme
import pt.carrismetropolitana.mobile.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.core.graphics.toColorInt
import androidx.activity.enableEdgeToEdge
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import pt.carrismetropolitana.mobile.composables.BigRoundedButton
//import pt.carrismetropolitana.mobile.composables.FavoriteCustomizationView
import pt.carrismetropolitana.mobile.composables.FavoriteItemCustomization
import pt.carrismetropolitana.mobile.composables.FavoriteType
import pt.carrismetropolitana.mobile.composables.WrappingCarousel
import pt.carrismetropolitana.mobile.composables.components.internal_widgets.FavoriteLineWidget
import pt.carrismetropolitana.mobile.composables.components.internal_widgets.FavoriteStopWidget
import pt.carrismetropolitana.mobile.composables.dummyItems
import pt.carrismetropolitana.mobile.composables.screens.AboutStopView
import pt.carrismetropolitana.mobile.composables.screens.LineDetailsView
import pt.carrismetropolitana.mobile.composables.screens.SplashScreen
import pt.carrismetropolitana.mobile.composables.screens.more.ENCMView
import pt.carrismetropolitana.mobile.composables.screens.more.FAQView
import pt.carrismetropolitana.mobile.services.cmapi.CMAPI
import pt.carrismetropolitana.mobile.services.cmapi.RetrofitInstance
import pt.carrismetropolitana.mobile.views.MapLibreMapView

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

sealed class Screens(val route : String) {
    object Splash: Screens("splash")
    object Home : Screens("home")
    object Lines : Screens("lines")
    object Stops : Screens("stops")


    object More: Screens("more")
    object ENCM: Screens("encm")
    object FAQ: Screens("faq")

    object LineDetails: Screens("line_details/{lineId}")
    object StopDetails: Screens("stop_details")

    object FavoriteCustomization: Screens("favorite_customization")
    object FavoriteItemCustomization: Screens("favorite_item_customization/{favoriteType}/{favoriteId}")
}

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private val cmApi = RetrofitInstance.cmApi
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        installSplashScreen()
//        window.setBackgroundDrawable(BitmapDrawable())
        super.onCreate(savedInstanceState)
        setContent {
            CarrisMetropolitanaTheme {
                val items = listOf<BottomNavigationItem>(
                    BottomNavigationItem(
                        title = "Home",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home,
                        route = Screens.Home.route
                    ),
                    BottomNavigationItem(
                        title = "Linhas",
                        selectedIcon = ImageVector.vectorResource(R.drawable.route_filled),
                        unselectedIcon = ImageVector.vectorResource(R.drawable.route_outlined),
                        route = Screens.Lines.route
                    ),
                    BottomNavigationItem(
                        title = "Paragens",
                        selectedIcon = ImageVector.vectorResource(R.drawable.map_filled),
                        unselectedIcon = ImageVector.vectorResource(R.drawable.map_outlined),
                        route = Screens.Stops.route
                    ),
                    BottomNavigationItem(
                        title = "Mais",
                        selectedIcon = ImageVector.vectorResource(R.drawable.more_horiz_filled),
                        unselectedIcon = ImageVector.vectorResource(R.drawable.more_horiz_filled),
                        route = Screens.More.route
                    ),
                )
                var selectedItemIndex by rememberSaveable {
                    mutableIntStateOf(0)
                }

                var topBarVisible by rememberSaveable {
                    mutableStateOf(false)
                }

                var bottomNavbarVisible by rememberSaveable {
                    mutableStateOf(false)
                }

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    Scaffold(
                        topBar = {
                            AnimatedVisibility(
                                visible = topBarVisible,
                                enter = slideInVertically(initialOffsetY = { -it }),
                                exit = slideOutVertically(targetOffsetY = { -it }),
                            ) {
                                TopAppBar(
                                    modifier = Modifier.height(120.dp),
                                    colors = TopAppBarDefaults.topAppBarColors(
                                        containerColor = Color("#FFDD01".toColorInt()),
                                        // titleContentColor = MaterialTheme.colorScheme.secondary,
                                    ),
                                    title = {
                                        Image(
                                            painter = painterResource(id = R.drawable.cm_logo_white),
                                            contentDescription = "Logo Carris Metropolitana",
                                            modifier = Modifier.padding(vertical = 10.dp)
                                        )
                                    },
                                )
                            }
                        },
                        bottomBar = {
                            AnimatedVisibility(
                                visible = bottomNavbarVisible,
                                enter = slideInVertically(initialOffsetY = { it }),
                                exit = slideOutVertically(targetOffsetY = { it })
                            ) {
                                NavigationBar() {
                                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                                    val currentDestination = navBackStackEntry?.destination

//                                when(navBackStackEntry?.destination?.route) {
//                                    Screens.Home.route -> {
//                                        topBarVisible = true
//                                    }
//                                    Screens.Lines.route -> {
//                                        topBarVisible = true
//                                    }
//                                    Screens.Stops.route -> {
//                                        topBarVisible = false
//                                    }
//                                    Screens.More.route -> {
//                                        topBarVisible = false
//                                    }
//                                }

                                    items.forEachIndexed { index, item ->
                                        NavigationBarItem(
                                            selected = selectedItemIndex == index,
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = Color.Black,
                                                unselectedIconColor = if (isSystemInDarkTheme()) Color.LightGray else Color.Black,
                                                selectedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                                                unselectedTextColor = if (isSystemInDarkTheme()) Color.LightGray else Color.Black,
                                                indicatorColor = Color("#FFDD01".toColorInt()),
                                            ),
                                            onClick = {
                                                selectedItemIndex = index

//                                                bottomNavbarVisible = item.route != Screens.Splash.route
//
//                                                if (item.route == Screens.Home.route) {
//                                                    topBarVisible = true
//                                                } else {
//                                                    topBarVisible = false
//                                                }

                                                if (item.route == Screens.Stops.route) {
                                                    window.statusBarColor =
                                                        Color(0x801B1B1B).toArgb()
                                                } else {
                                                    window.statusBarColor =
                                                        Color.Unspecified.toArgb()
                                                }

                                                navController.navigate(item.route) {
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            },
                                            label = {
                                                Text(text = item.title)
                                            },
                                            icon = {
                                                Icon(
                                                    imageVector = if (selectedItemIndex == index) item.selectedIcon else item.unselectedIcon,
                                                    contentDescription = item.title
                                                )
                                            }
                                        )

                                    }
                                }
                            }
                        },
                    ) { padding ->
                        NavHost(
                            navController = navController,
                            startDestination = Screens.Splash.route,
                        ) {
                            composable(
                                Screens.Splash.route
                            ) {
                                LaunchedEffect(Unit) {
                                    topBarVisible = false
                                    bottomNavbarVisible = false
                                }
                                SplashScreen(navController = navController)
                            }
                            composable(
                                Screens.Home.route,
//                                enterTransition = {
//                                    scaleIntoContainer()
//                                },
//                                exitTransition = {
//                                    scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
//                                },
//                                popEnterTransition = {
//                                    scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
//                                },
//                                popExitTransition = {
//                                    scaleOutOfContainer()
//                                }
                            ) {
                                LaunchedEffect(Unit) {
                                    topBarVisible = true
                                    bottomNavbarVisible = true
                                }
                                HomeScreen(navController, padding)
                            }
                            composable(
                                Screens.Lines.route,
//                                enterTransition = {
//                                    scaleIntoContainer()
//                                },
//                                exitTransition = {
//                                    scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
//                                },
//                                popEnterTransition = {
//                                    scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
//                                },
//                                popExitTransition = {
//                                    scaleOutOfContainer()
//                                }
                            ) {
                                LaunchedEffect(Unit) {
                                    topBarVisible = false
                                    bottomNavbarVisible = true
                                }
                                LinesScreen(navController, padding)
                            }
                            composable(
                                Screens.Stops.route,
//                                enterTransition = {
//                                    scaleIntoContainer()
//                                },
//                                exitTransition = {
//                                    scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
//                                },
//                                popEnterTransition = {
//                                    scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
//                                },
//                                popExitTransition = {
//                                    scaleOutOfContainer()
//                                }
                            ) {
                                LaunchedEffect(Unit) {
                                    topBarVisible = false
                                    bottomNavbarVisible = true
                                }
                                StopsScreen(navController, cmApi)
                            }
                            composable(
                                Screens.More.route,
//                                enterTransition = {
//                                    scaleIntoContainer()
//                                },
//                                exitTransition = {
//                                    scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
//                                },
//                                popEnterTransition = {
//                                    scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
//                                },
//                                popExitTransition = {
//                                    scaleOutOfContainer()
//                                }
                            ) {
                                LaunchedEffect(Unit) {
                                    topBarVisible = false
                                    bottomNavbarVisible = true
                                }
                                MoreScreen(navController, padding, context = this@MainActivity)
                            }


                            composable(
                                Screens.LineDetails.route
                            ) {backStackEntry ->
                                backStackEntry.arguments?.getString("lineId")
                                    ?.let { LineDetailsView(lineId = it, navController = navController) }
                            }

                            composable(Screens.StopDetails.route) {
                                AboutStopView(navController = navController)
                            }


                            composable(
                                Screens.ENCM.route
                            ) {
                                ENCMView(navController = navController)
                            }

                            composable(
                                Screens.FAQ.route
                            ) {
                                FAQView(navController = navController, paddingValues = padding)
                            }

                            composable(
                                Screens.FavoriteItemCustomization.route
                            ) {backStackEntry ->
                                backStackEntry.arguments?.getString("favoriteId")
                                    ?.let { favoriteId ->
                                        backStackEntry.arguments?.getString("favoriteType")
                                            ?.let { favoriteType ->
                                                FavoriteItemCustomization(navController = navController, favoriteType = FavoriteType.valueOf(favoriteType), favoriteId = favoriteId)
                                            }
                                    }
                            }


//                            dialog(Screens.FavoriteCustomization.route) {
//                                FavoriteCustomizationView(navController = navController)
//                            }


                        }
                    }
                }
            }
        }
    }
}

enum class ScaleTransitionDirection {
    INWARDS, OUTWARDS
}

fun scaleIntoContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.INWARDS,
    initialScale: Float = if (direction == ScaleTransitionDirection.OUTWARDS) 0.9f else 1.1f
): EnterTransition {
    return scaleIn(
        animationSpec = tween(220, delayMillis = 90),
        initialScale = initialScale
    ) + fadeIn(animationSpec = tween(220, delayMillis = 90))
}

fun scaleOutOfContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.OUTWARDS,
    targetScale: Float = if (direction == ScaleTransitionDirection.INWARDS) 0.9f else 1.1f
): ExitTransition {
    return scaleOut(
        animationSpec = tween(
            durationMillis = 220,
            delayMillis = 90
        ), targetScale = targetScale
    ) + fadeOut(tween(delayMillis = 90))
}

@Composable
fun HomeScreen(navController: NavController, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color("#F0F0F0".toColorInt())),
//        verticalArrangement = Arrangement.SpaceAround,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row (horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
            Text("Favoritos", fontWeight = FontWeight.Bold, fontSize = 24.sp, modifier = Modifier.padding(12.dp))
        }
//        Text(
//            "A Carris Metropolitana está mais próxima",
//            textAlign = TextAlign.Center,
//            fontWeight = FontWeight.Black,
//            fontSize = 24.sp,
//            modifier = Modifier.padding(horizontal = 25.dp)
//        )
//        Text(
//            "Crie uma conta para personalizar a app com as suas linhas e paragens favoritas.",
//            modifier = Modifier.padding(horizontal = 12.dp),
//            textAlign = TextAlign.Center,
//        )
//        Text(
//            "Saiba exatamente onde andam todos os autocarros e exatamente quando chegam à sua paragem.",
//            modifier = Modifier.padding(24.dp),
//            textAlign = TextAlign.Center
//        )

        Column(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            FavoriteStopWidget(navController = navController)

            Spacer(modifier = Modifier.height(32.dp))

            FavoriteLineWidget(navController = navController)
        }
    }
}


//@Serializable
//data class Line(
//    @SerialName("id") val id: String,
//    @SerialName("short_name") val shortName: String,
//    @SerialName("long_name") val longName: String,
//    @SerialName("color") val color: String,
//    @SerialName("text_color") val textColor: String,
//    @SerialName("routes") val routes: List<String>,
//    @SerialName("patterns") val patterns: List<String>,
//    @SerialName("municipalities") val municipalities: List<String>,
//    @SerialName("localities") val localities: List<String?>,
//    @SerialName("facilities") val facilities: List<Facility>
//)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinesScreen(navController: NavController, parentPaddingValues: PaddingValues) {
    var text by rememberSaveable {
        mutableStateOf("")
    }

    var active by rememberSaveable {
        mutableStateOf(false)
    }
    
    Scaffold(
        topBar = {
            Column {
                AnimatedVisibility(visible = !active) {
                    MediumTopAppBar(
                        title = {
                            Text("Linhas")
                        },
                        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color("#FFDD01".toColorInt()))
                    )
                }
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
                        Text(text = "Pesquisar linhas")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color("#FFDD01".toColorInt()))
                        .padding(horizontal = if (active) 0.dp else 12.dp)
                        .padding(bottom = if (active) 0.dp else 12.dp)
                        .padding(top = if (active) parentPaddingValues.calculateTopPadding() else 0.dp),
                    windowInsets = WindowInsets(0, 0, 0, 0)
                ) {

                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
                DummyLinesList(transitLines = dummyTransitLines, navController = navController)
        }
    }
}

@Composable
fun MLNMapView(modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            MapLibre.getInstance(context)
            val mapView = MapView(context)
            mapView.onCreate(null)
            mapView.getMapAsync { map ->
                // Set the style after mapView was loaded
                map.setStyle("https://maps.carrismetropolitana.pt/styles/default/style.json") {
                    // Hide attributions
                    map.uiSettings.isAttributionEnabled = false
                    map.uiSettings.isLogoEnabled = false
                    // Set the map view center
                    map.cameraPosition = CameraPosition.Builder()
                        .target(LatLng(38.7, -9.0))
                        .zoom(8.9)
                        .build()
                }
            }
            mapView
        },
        modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StopsScreen(navController: NavController, cmApi: CMAPI) {
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 150.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { navController.navigate("stop_details") })  {
                    Text("Ver detalhes da paragem de demonstração")
                }
            }
        }
    }
}

@Composable
fun MoreScreen(navController: NavController, paddingValues: PaddingValues, context: Context) {
    Scaffold { innerPaddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddingValues)
                .padding(bottom = paddingValues.calculateBottomPadding())
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Novidades",
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.sp,
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(24.dp),
            )

            WrappingCarousel(items = dummyItems)

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Informar",
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.sp,
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 24.dp),
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(12.dp)
            ) {
                BigRoundedButton(
                    text = "Espaços navegante®",
                    icon = ImageVector.vectorResource(R.drawable.phosphoricons_house_duotone),
                    iconContentDescription = "Rounded Home Icon",
                    iconTint = Color("#5956d6".toColorInt())
                ) {
                    println("Button pressed")
                    navController.navigate(Screens.ENCM.route)
                }
                BigRoundedButton(
                    text = "Perguntas Frequentes",
                    icon = ImageVector.vectorResource(R.drawable.phosphoricons_question_duotone),
                    iconContentDescription = "Help Icon",
                    iconTint = Color("#ff9500".toColorInt())
                ) {
                    println("Button pressed")
                    navController.navigate(Screens.FAQ.route)
                }
                BigRoundedButton(
                    text = "Apoio ao Cliente",
                    icon = ImageVector.vectorResource(R.drawable.phosphoricons_chat_dots_duotone),
                    iconTint = Color("#007aff".toColorInt()),
                    iconContentDescription = "Support Agent Icon",
                    externalLink = true
                ) {
                    println("Button pressed")
                    CustomTabsIntent.Builder().build()
                        .launchUrl(context, Uri.parse("https://www.carrismetropolitana.pt/apoio/"))
                }
            }

            Text(
                text = "Viajar",
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.sp,
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 24.dp)
                    .padding(top = 12.dp),
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(12.dp)
            ) {
                BigRoundedButton(
                    text = "Carregar o Passe",
                    icon = ImageVector.vectorResource(R.drawable.phosphoricons_credit_card_duotone),
                    iconContentDescription = "Credit Card Icon",
                    iconTint = Color.Red
                ) {
                    println("Button pressed")
                }
                BigRoundedButton(
                    text = "Cartões e Descontos",
                    icon = ImageVector.vectorResource(R.drawable.phosphoricons_lightning_duotone),
                    iconContentDescription = "Lightning Icon",
                    iconTint = Color.Red
                ) {
                    println("Button pressed")
                }
                BigRoundedButton(
                    text = "Tarifários",
                    icon = ImageVector.vectorResource(R.drawable.phosphoricons_coins_duotone),
                    iconTint = Color.Red,
                    iconContentDescription = "Support Agent Icon"
                ) {
                    println("Button pressed")
                    CustomTabsIntent.Builder().build()
                        .launchUrl(context, Uri.parse("https://www.carrismetropolitana.pt/apoio/"))
                }
            }

            Text(
                text = "Carris Metropolitana",
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.sp,
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 24.dp)
                    .padding(top = 12.dp),
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(12.dp)
            ) {
                BigRoundedButton(
                    text = "Recrutamento",
                    icon = ImageVector.vectorResource(R.drawable.phosphoricons_user_check_duotone),
                    iconContentDescription = "Credit Card Icon",
                    iconTint = Color("#ffcc00".toColorInt()),
                    externalLink = true
                ) {
                    CustomTabsIntent.Builder().build()
                        .launchUrl(context, Uri.parse("https://www.carrismetropolitana.pt/motoristas/"))
                }
                BigRoundedButton(
                    text = "Dados Abertos",
                    icon = ImageVector.vectorResource(R.drawable.phosphoricons_magic_wand_duotone),
                    iconContentDescription = "Lightning Icon",
                    iconTint = Color("#007aff".toColorInt()),
                    externalLink = true
                ) {
                    CustomTabsIntent.Builder().build()
                        .launchUrl(context, Uri.parse("https://www.carrismetropolitana.pt/opendata/"))
                }
                BigRoundedButton(
                    text = "Privacidade",
                    icon = ImageVector.vectorResource(R.drawable.phosphoricons_lock_duotone),
                    iconTint = Color("#007aff".toColorInt()),
                    iconContentDescription = "Support Agent Icon",
                    externalLink = true
                ) {
                    CustomTabsIntent.Builder().build().launchUrl(
                        context,
                        Uri.parse("https://www.carrismetropolitana.pt/politica-de-privacidade/")
                    )
                }
                BigRoundedButton(
                    text = "Aviso Legal",
                    icon = ImageVector.vectorResource(R.drawable.phosphoricons_seal_check_duotone),
                    iconTint = Color("#007aff".toColorInt()),
                    iconContentDescription = "Support Agent Icon",
                    externalLink = true
                ) {
                    CustomTabsIntent.Builder().build().launchUrl(
                        context,
                        Uri.parse("https://www.carrismetropolitana.pt/aviso-legal/")
                    )
                }
            }
        }
    }
}



@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CarrisMetropolitanaTheme {
        LinesScreen(navController = rememberNavController(), parentPaddingValues = PaddingValues(0.dp))
    }
}
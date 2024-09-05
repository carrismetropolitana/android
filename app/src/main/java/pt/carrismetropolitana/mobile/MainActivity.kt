package pt.carrismetropolitana.mobile

import android.os.Bundle
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
import androidx.compose.ui.res.vectorResource
import androidx.core.graphics.toColorInt
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
//import pt.carrismetropolitana.mobile.composables.FavoriteCustomizationView
import pt.carrismetropolitana.mobile.composables.FavoriteItemCustomization
import pt.carrismetropolitana.mobile.composables.components.news.NewsView
import pt.carrismetropolitana.mobile.composables.components.transit.alerts.AlertsFilterForInformedEntities
import pt.carrismetropolitana.mobile.composables.components.transit.alerts.AlertsView
import pt.carrismetropolitana.mobile.composables.screens.stops.AboutStopView
import pt.carrismetropolitana.mobile.composables.screens.lines.LineDetailsView
import pt.carrismetropolitana.mobile.composables.screens.SplashScreen
import pt.carrismetropolitana.mobile.composables.components.favorites.FavoritesCustomization
import pt.carrismetropolitana.mobile.composables.components.favorites.SelectFavoriteLineView
import pt.carrismetropolitana.mobile.composables.components.favorites.SelectFavoriteStopView
import pt.carrismetropolitana.mobile.composables.components.transit.vehicles.VehicleRealtimeTrackingView
import pt.carrismetropolitana.mobile.composables.screens.home.HomeScreen
import pt.carrismetropolitana.mobile.composables.screens.lines.LinesScreen
import pt.carrismetropolitana.mobile.composables.screens.more.ENCMView
import pt.carrismetropolitana.mobile.composables.screens.more.FAQView
import pt.carrismetropolitana.mobile.composables.screens.more.MoreScreen
import pt.carrismetropolitana.mobile.composables.screens.stops.StopsScreen
import pt.carrismetropolitana.mobile.managers.AlertsManager
import pt.carrismetropolitana.mobile.managers.FavoritesManager
import pt.carrismetropolitana.mobile.managers.LinesManager
import pt.carrismetropolitana.mobile.managers.StopsManager
import pt.carrismetropolitana.mobile.managers.VehiclesManager
import pt.carrismetropolitana.mobile.services.database.AppDatabase
import pt.carrismetropolitana.mobile.services.favorites.FavoriteType
import pt.carrismetropolitana.mobile.ui.common.animatedComposable
import pt.carrismetropolitana.mobile.ui.common.slideInVerticallyComposable

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

sealed class Screens(val route : String) {
    // NavBar main destinations
    object Splash: Screens("splash")
    object Home : Screens("home")
    object Lines : Screens("lines")
    object Stops : Screens("stops")

    object More: Screens("more")

    // More screen sub-destinations
    object ENCM: Screens("encm")
    object FAQ: Screens("faq")
    object News: Screens("news?url={url}")

    // Common sub-destinations
    object LineDetails: Screens("line_details/{lineId}?overridePatternId={overridePatternId}")
    object StopDetails: Screens("stop_details/{stopId}")

    // Favorite destinations
    object FavoritesCustomization: Screens("favorites_customization")
    object FavoriteItemCustomization: Screens("favorite_item_customization/{favoriteType}?favoriteId={favoriteId}")
    object SelectFavoriteLine: Screens("select_favorite_line")
    object SelectFavoriteStop: Screens("select_favorite_stop")

    // Alerts destinations
    object AlertsForEntity: Screens("alerts_for_entity/{entityType}/{entityId}")

    object VehicleRealtimeTracking: Screens("vehicle_realtime_tracking/{vehicleId}")
}

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private val linesManager by lazy { LinesManager() }
    private val stopsManager by lazy { StopsManager() }
    private val alertsManager by lazy { AlertsManager() }
    private val vehiclesManager by lazy { VehiclesManager() }

    private val favoritesManager by lazy { FavoritesManager(Room.databaseBuilder(
        this,
        AppDatabase::class.java,
        "favorites_database"
    ).build().favoriteDao()) }

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

                CompositionLocalProvider(
                    LocalLinesManager provides linesManager,
                    LocalStopsManager provides stopsManager,
                    LocalAlertsManager provides alertsManager,
                    LocalVehiclesManager provides vehiclesManager,
                    LocalFavoritesManager provides favoritesManager
                ) {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController = rememberNavController()
                        Scaffold(
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
                                ) {
                                    LaunchedEffect(Unit) {
                                        topBarVisible = true
                                        bottomNavbarVisible = true
                                    }
                                    HomeScreen(parentPaddingValues = padding, navController = navController)
                                }
                                composable(
                                    Screens.Lines.route,
                                ) {
                                    LaunchedEffect(Unit) {
                                        topBarVisible = false
                                        bottomNavbarVisible = true
                                    }
                                    LinesScreen(navController, padding)
                                }
                                composable(
                                    Screens.Stops.route,
                                ) {
                                    LaunchedEffect(Unit) {
                                        topBarVisible = false
                                        bottomNavbarVisible = true
                                    }
                                    StopsScreen(navController)
                                }
                                composable(
                                    Screens.More.route,
                                ) {
                                    LaunchedEffect(Unit) {
                                        topBarVisible = false
                                        bottomNavbarVisible = true
                                    }
                                    MoreScreen(navController, padding, context = this@MainActivity)
                                }


                                animatedComposable(
                                    Screens.LineDetails.route,
                                ) {backStackEntry ->
                                    LaunchedEffect(Unit) {
                                        bottomNavbarVisible = true
                                    }
                                    backStackEntry.arguments?.getString("lineId")
                                        ?.let { LineDetailsView(lineId = it, overrideDisplayedPatternId = backStackEntry.arguments?.getString("overridePatternId"), navController = navController, parentPadding = padding) }
                                }

                                animatedComposable(
                                    Screens.StopDetails.route
                                ) {
                                    LaunchedEffect(Unit) {
                                        bottomNavbarVisible = true
                                    }
                                    it.arguments?.getString("stopId")
                                        ?.let { AboutStopView(stopId = it, navController = navController, parentPadding = padding) }
                                }


                                animatedComposable(
                                    Screens.ENCM.route,
                                ) {
                                    ENCMView(navController = navController)
                                }

                                animatedComposable(
                                    Screens.FAQ.route
                                ) {
                                    FAQView(navController = navController, paddingValues = padding)
                                }

                                slideInVerticallyComposable(
                                    Screens.FavoritesCustomization.route
                                ) {
                                    LaunchedEffect(Unit) {
                                        bottomNavbarVisible = false
                                    }
                                    FavoritesCustomization(navController = navController, onCloseButtonClick = { navController.popBackStack() })
                                }

                                animatedComposable(
                                    Screens.SelectFavoriteLine.route
                                ) {
                                    LaunchedEffect(Unit) {
                                        bottomNavbarVisible = false
                                    }
                                    SelectFavoriteLineView(navController = navController)
                                }

                                animatedComposable(
                                    Screens.SelectFavoriteStop.route
                                ) {
                                    LaunchedEffect(Unit) {
                                        bottomNavbarVisible = false
                                    }
                                    SelectFavoriteStopView(navController = navController)
                                }

                                slideInVerticallyComposable(
                                    Screens.FavoriteItemCustomization.route
                                ) {backStackEntry ->
                                    LaunchedEffect(Unit) {
                                        bottomNavbarVisible = false
                                    }


                                    backStackEntry.arguments?.getString("favoriteType")
                                        ?.let { favoriteType ->
                                            FavoriteItemCustomization(navController = navController, favoriteType = FavoriteType.valueOf(favoriteType), favoriteId = backStackEntry.arguments?.getString("favoriteId"))
                                        }
                                }

                                animatedComposable(
                                    Screens.AlertsForEntity.route
                                ) {backStackEntry ->
                                    LaunchedEffect(Unit) {
                                        bottomNavbarVisible = false
                                    }

                                    backStackEntry.arguments?.getString("entityType")
                                        ?.let { entityType ->
                                            backStackEntry.arguments?.getString("entityId")
                                                ?.let { entityId ->
                                                    AlertsView(
                                                        alertEntities = alertsManager.data.collectAsState().value,
                                                        filterFor = AlertsFilterForInformedEntities.valueOf(entityType),
                                                        filterByEntityId = entityId,
                                                        navController = navController
                                                    )
                                                }
                                        }
                                }

                                animatedComposable(
                                    Screens.News.route
                                ) {
                                    LaunchedEffect(Unit) {
                                        bottomNavbarVisible = false
                                    }
                                    it.arguments?.getString("url")
                                        ?.let { url ->
                                            NewsView(newsUrl = url, navController = navController)
                                        }
                                }

                                animatedComposable(
                                    Screens.VehicleRealtimeTracking.route
                                ) {
                                    LaunchedEffect(Unit) {
                                        bottomNavbarVisible = false
                                    }
                                    it.arguments?.getString("vehicleId")
                                        ?.let { vehicleId ->
                                            VehicleRealtimeTrackingView(vehicleId = vehicleId, navController = navController)
                                        }
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CarrisMetropolitanaTheme {
        LinesScreen(navController = rememberNavController(), parentPaddingValues = PaddingValues(0.dp))
    }
}
package pt.carrismetropolitana.mobile.composables.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import pt.carrismetropolitana.mobile.LocalFavoritesManager
import pt.carrismetropolitana.mobile.R
import pt.carrismetropolitana.mobile.composables.components.FavoritesCustomizationButton
import pt.carrismetropolitana.mobile.composables.components.internal_widgets.FavoriteLineWidget
import pt.carrismetropolitana.mobile.composables.components.internal_widgets.FavoriteStopWidget
import pt.carrismetropolitana.mobile.services.favorites.FavoriteType
import pt.carrismetropolitana.mobile.ui.theme.CMSystemBackground100

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val favoritesManager = LocalFavoritesManager.current

    Scaffold(
        topBar = {
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(CMSystemBackground100),
//        verticalArrangement = Arrangement.SpaceAround,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            if (favoritesManager.favorites.isEmpty()) {
                UnregisteredUserView()
            } else {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text("Favoritos", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    FavoritesCustomizationButton(onClick = { /*TODO*/ })
                }

                Column(
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    for (favorite in favoritesManager.favorites) {
                        when (favorite.type) {
                            FavoriteType.STOP -> FavoriteStopWidget(navController = navController)
                            FavoriteType.PATTERN -> FavoriteLineWidget(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
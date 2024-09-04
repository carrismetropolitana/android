package pt.carrismetropolitana.mobile.composables.components.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pt.carrismetropolitana.mobile.LocalFavoritesManager
import pt.carrismetropolitana.mobile.LocalLinesManager
import pt.carrismetropolitana.mobile.services.favorites.FavoriteItem
import pt.carrismetropolitana.mobile.services.favorites.FavoriteType
import pt.carrismetropolitana.mobile.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesCustomization(
    navController: NavController,
    onCloseButtonClick: () -> Unit
) {
    val favoritesManager = LocalFavoritesManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Personalizar") },
                actions = {
                    IconButton(onClick = { onCloseButtonClick() }) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                Text(
                    "Ordenar Cartões",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Organize os cartões como quer que apareçam na página principal. Altere a ordem deslizando no ícone ≡",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(favoritesManager.favorites) { item ->
                FavoriteItemCard(item, onClick = {
                    navController.navigate("favorite_item_customization/${item.type.name}?favoriteId=${item.stopId ?: item.lineId}")
                })
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Adicionar novo cartão",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Escolha um tipo de cartão para adicionar à página principal.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(getNewCardOptions()) { option ->
                NewCardOptionButton(option, onClick = { favoriteType ->
                    navController.navigate("favorite_item_customization/${option.favoriteType.name}")
                })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun FavoriteItemCard(item: FavoriteItem, onClick: () -> Unit) {
    val linesManager = LocalLinesManager.current
    Card(elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp), modifier = Modifier.clickable {

    }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.drag_handle),
                contentDescription = "Reorder"
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("${if (item.type == FavoriteType.PATTERN) "Linha" else "Paragem"} favorita", fontSize = 12.sp)
                if (item.type == FavoriteType.PATTERN) {
                    val line = linesManager.data.collectAsState().value.firstOrNull { it.id == item.lineId }
                    Text(line?.longName ?: "", fontWeight = FontWeight.Bold)
                } else {
                    Text(item.displayName!!, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun NewCardOptionButton(option: NewCardOption, onClick: (favoriteType: FavoriteType) -> Unit) {
    Button(
        onClick = { onClick(option.favoriteType) },
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(option.title)
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
}

data class NewCardOption(val favoriteType: FavoriteType, val title: String)

fun getNewCardOptions(): List<NewCardOption> = listOf(
    NewCardOption(FavoriteType.STOP,"Paragem Favorita"),
    NewCardOption(FavoriteType.PATTERN,"Linha Favorita")
)
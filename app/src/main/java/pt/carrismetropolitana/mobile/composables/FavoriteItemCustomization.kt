package pt.carrismetropolitana.mobile.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

enum class FavoriteType {
    LINE,
    STOP
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteItemCustomization(
    navController: NavController,
    favoriteType: FavoriteType,
    favoriteId: String
) {
    var receiveNotifications by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("${if (favoriteType == FavoriteType.STOP) "Paragem" else "Linha"} favorita")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Dismiss")
                    }
                },
                actions = {
                    Text(text = "Guardar", modifier = Modifier.clickable { navController.popBackStack() })
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    "Selecionar ${if (favoriteType == FavoriteType.STOP) "paragem" else "linha"}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
                Text("Escolha uma ${if (favoriteType == FavoriteType.STOP) "paragem" else "linha"} para visualizar na página principal.")


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .clip(shape = RoundedCornerShape(size = 12.dp))
                        .background(Color.LightGray)
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                        Text("Pesquisar ${if (favoriteType == FavoriteType.STOP) "paragens" else "linhas"}")
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    "Selecionar destino${if (favoriteType == FavoriteType.STOP) "s" else ""}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
                Text(if (favoriteType == FavoriteType.STOP) "Escolha 1 destino para gravar na página de entrada." else "Escolha quais destinos pretende visualizar.")

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .clip(shape = RoundedCornerShape(size = 12.dp))
                        .background(Color.LightGray)
                        .padding(horizontal = 12.dp, vertical = 20.dp)
                ) {
                    Text("Selecione uma ${if (favoriteType == FavoriteType.STOP) "paragem" else "linha"}", color = Color.Gray)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    "Ativar notificações",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
                Text("Receber notificações sempre que houver novos avisos para as linhas e paragens selecionadas.")

                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .clip(shape = RoundedCornerShape(size = 12.dp))
                        .background(Color.LightGray)
                        .padding(horizontal = 12.dp, vertical = 5.dp),
                ) {
                    Text("Receber notificações")
                    Switch(checked = receiveNotifications, onCheckedChange = { receiveNotifications = it })
                }
            }
        }
    }
}

@Preview
@Composable
fun FavoriteItemCustomizationPreview() {
    FavoriteItemCustomization(
        navController = rememberNavController(),
        favoriteType = FavoriteType.LINE,
        favoriteId = "1523"
    )
}
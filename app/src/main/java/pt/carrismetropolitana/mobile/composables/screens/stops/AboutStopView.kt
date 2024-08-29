package pt.carrismetropolitana.mobile.composables.screens.stops

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import pt.carrismetropolitana.mobile.LinesList
import pt.carrismetropolitana.mobile.R
import pt.carrismetropolitana.mobile.TransitLine
import pt.carrismetropolitana.mobile.composables.screens.lines.SquareButton


val dummyTransitPatterns = listOf(
    TransitLine("1523", "Agualva Cacém (Estação)", Color.White, Color("#C61D23".toColorInt())),
    TransitLine("1109", "Carnaxide", Color.White, Color("#3D85C6".toColorInt())),
    TransitLine("4906", "Setúbal (ITS)", Color.White, Color("#BB3E96".toColorInt())),
//    TransitLine("CP", "Oeiras - São Pedro (Serviço Ocasional)", Color.White, Color("#2A9057".toColorInt()))
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutStopView(
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Paragem")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
//                        val sendIntent: Intent = Intent(Intent.ACTION_SEND, Uri.parse("https://beta.carrismetropolitana.pt/lines/1523"))
//                        context.startActivity(sendIntent)
                    }) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp),

                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier.border(
                            1.dp,
                            Color.Gray,
                            shape = RoundedCornerShape(24.dp)
                        )
                    ) {
                        Text(
                            "123456",
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                    Text(
                        "38.718273",
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                    Text(
                        "-9.012345",
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }

                Text("Sete Rios (Estação) P7", fontWeight = FontWeight.Bold, fontSize = 24.sp)

                Row {
                    SquareButton(
                        icon = ImageVector.vectorResource(R.drawable.phosphoricons_star),
                        iconTint = Color("#ffcc00".toColorInt()),
                        iconContentDescription = "Favorite Stop Icon",
                        size = 60
                    ) {
                        navController.navigate("favorite_item_customization/STOP/123456")
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding()
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text(
                        "Destinos a partir desta paragem",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }

            LinesList(transitLines = dummyTransitPatterns, navController = navController)

        }
    }
}
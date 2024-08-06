package pt.carrismetropolitana.mobile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pt.carrismetropolitana.mobile.composables.components.Pill

// Dummy data class for transit lines
data class TransitLine(
    val shortName: String,
    val longName: String,
    val textColor: Color,
    val pillColor: Color
)

// Dummy transit lines data
val dummyTransitLines = listOf(
    TransitLine("1523", "Agualva Cacém (Estação) - Oeiras (Estação)", Color.White, Color("#C61D23".toColorInt())),
    TransitLine("1109", "Carnaxide via Outurela | Circular", Color.White, Color("#3D85C6".toColorInt())),
    TransitLine("4906", "Setúbal (ITS) - Vendas Novas via Landeira", Color.White, Color("#BB3E96".toColorInt())),
//    TransitLine("CP", "Oeiras - São Pedro (Serviço Ocasional)", Color.White, Color("#2A9057".toColorInt()))
)

@Composable
fun DummyLinesList(transitLines: List<TransitLine>, navController: NavController) {
    Column() {
        transitLines.forEachIndexed { index, line ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { navController.navigate("line_details/${line.shortName}") }
            ) {
                Pill(text = line.shortName, color = line.pillColor, textColor = line.textColor, size = 60)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = line.longName,
                    fontSize = 16.sp, // Set font size in scaled pixels (sp)
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }
            if (index < transitLines.size - 1) {
                Divider(color = Color.LightGray, thickness = 1.dp)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview
@Composable
fun DummyLinesListPreview() {
    DummyLinesList(transitLines = dummyTransitLines, navController = rememberNavController())
}
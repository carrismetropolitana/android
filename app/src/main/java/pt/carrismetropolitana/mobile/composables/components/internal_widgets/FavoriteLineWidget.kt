package pt.carrismetropolitana.mobile.composables.components.internal_widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pt.carrismetropolitana.mobile.composables.components.Pill
import pt.carrismetropolitana.mobile.composables.screens.stops.MLNMapView

@Composable
fun FavoriteLineWidget(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
//                .clickable { navController.navigate("line_details/1523") },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Pill(text = "1523", color = Color("#C61D23".toColorInt()), textColor = Color.White, size = 60)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Agualva Cacém (Estação) - Oeiras (Estação)", fontWeight = FontWeight.Bold,
                fontSize = 14.sp // Set font size in scaled pixels (sp)
            )
        }

        HorizontalDivider()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            MLNMapView()
        }
    }
}

@Preview
@Composable
fun FavoriteLineWidgetPreview() {
    FavoriteLineWidget(navController = rememberNavController())
}
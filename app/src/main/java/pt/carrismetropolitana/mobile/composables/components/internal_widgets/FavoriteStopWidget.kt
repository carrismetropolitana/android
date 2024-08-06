package pt.carrismetropolitana.mobile.composables.components.internal_widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pt.carrismetropolitana.mobile.R
import pt.carrismetropolitana.mobile.composables.components.Pill


@Composable
fun FavoriteStopWidget(
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
//                modifier = Modifier
//                    .clickable { navController.navigate("stop_details") }
            ) {
                Text("Alameda Edgar Cardoso", fontWeight = FontWeight.Bold)
                Text("Lisboa", color = Color.Gray)
            }

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.phosphoricons_star_fill),
                contentDescription = "Favorite",
                tint = Color("#ffcc00".toColorInt())
            )
        }

        HorizontalDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Pill(
                    text = "1523",
                    color = Color("#C61D23".toColorInt()),
                    textColor = Color.White,
                    size = 60
                )
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Arrow", Modifier.size(15.dp))
                Text(text = "Agualva - Cacém (Est...", fontWeight = FontWeight.Bold)
            }

            Text("5 min", fontWeight = FontWeight.Bold, color = Color.Green)
        }

        HorizontalDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Pill(
                    text = "1523",
                    color = Color("#C61D23".toColorInt()),
                    textColor = Color.White,
                    size = 60
                )
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Arrow", Modifier.size(15.dp))
                Text(text = "Agualva - Cacém (Est...", fontWeight = FontWeight.Bold)
            }

            Text("7 min", fontWeight = FontWeight.Bold, color = Color.Green)
        }
    }
}

@Preview
@Composable
fun FavoriteStopWidgetPreview() {
    FavoriteStopWidget(navController = rememberNavController())
}
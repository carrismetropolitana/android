package pt.carrismetropolitana.mobile.composables.components.transit.pattern_path

import android.graphics.drawable.Icon
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp

@Composable
fun PatternPathFooterButton(
    text: String,
    @DrawableRes iconResourceId: Int,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.LightGray,
            contentColor = Color.Gray
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row {
            ImageVector.vectorResource(id = iconResourceId)
            Text(text)
        }
    }
}
package pt.carrismetropolitana.mobile.composables.components.maps.overlays

import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

@Composable
fun MapFloatingButton(
    iconResourceId: Int,
    onClick: () -> Unit
) {
    FloatingActionButton(onClick = { onClick() }) {
        ImageVector.vectorResource(iconResourceId)
    }
}
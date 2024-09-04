package pt.carrismetropolitana.mobile.composables.components.transit.stops

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import pt.carrismetropolitana.mobile.services.cmapi.Stop

@Composable
fun StopsList(
    stops: List<Stop>,
    onStopClick: (stopId: String) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp)
    ) {
        items(stops) {
            StopsListItem(it, onStopClick = {
                onStopClick(it)
            })
        }
    }
}
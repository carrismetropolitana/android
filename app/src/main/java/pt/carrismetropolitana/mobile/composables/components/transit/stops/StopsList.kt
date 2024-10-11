package pt.carrismetropolitana.mobile.composables.components.transit.stops

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.carrismetropolitana.mobile.services.cmapi.Stop

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StopsList(
    modifier: Modifier = Modifier,
    stops: List<Stop>,
    onStopClick: (stopId: String) -> Unit,
    header: @Composable () -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        stickyHeader {
            header()
        }
        items(stops) {
            StopsListItem(it, onStopClick = {
                onStopClick(it)
            }, paddingValues = PaddingValues(horizontal = 12.dp))
        }
    }
}
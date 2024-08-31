package pt.carrismetropolitana.mobile.composables.components.transit.pattern_path

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.carrismetropolitana.mobile.composables.components.feedback.QuestionItem
import pt.carrismetropolitana.mobile.services.cmapi.CMAPI
import pt.carrismetropolitana.mobile.services.cmapi.PathEntry
import pt.carrismetropolitana.mobile.services.cmapi.PatternRealtimeETA
import java.util.Dictionary


@Composable
fun PatternPath(
    patternId: String?,
    pathItems: List<PathEntry>,
    pathColor: Color,
    onSchedulesButtonClick: (stopId: String) -> Unit,
    onStopDetailsButtonClick: (stopId: String) -> Unit,
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit,
) {
    var expandedPathItemIndex by remember { mutableIntStateOf(0) }
    val nextArrivalsByStop = remember { mutableStateOf(mapOf<String, List<PatternRealtimeETA>>()) }

    LaunchedEffect(Unit) {
        if (patternId == null) return@LaunchedEffect
        val nextArrivals = CMAPI.shared.getPatternETAs(patternId)
        nextArrivalsByStop.value = arrangeArrivalsByStop(nextArrivals)
    }

    LazyColumn(
        modifier = modifier,
    ) {
        item {
            header()
            Spacer(modifier = Modifier.height(16.dp))
        }
        itemsIndexed(pathItems) { index, pathItem ->
            val positionInList =
                if (index == 0) PositionInList.FIRST
                else if (index == pathItems.size - 1) PositionInList.LAST
                else PositionInList.MIDDLE

            PatternPathItem(
                pathItem = pathItem,
                pathColor = pathColor,
                expanded = index == expandedPathItemIndex,
                positionInList = positionInList,
                onClick = { expandedPathItemIndex = index },
                onSchedulesButtonClick = { onSchedulesButtonClick(pathItem.stop.id) },
                onStopDetailsButtonClick = { onStopDetailsButtonClick(pathItem.stop.id) },
                nextArrivals = nextArrivalsByStop.value[pathItem.stop.id] ?: listOf(),
            )
        }
    }
}

fun arrangeArrivalsByStop(arrivals: List<PatternRealtimeETA>): Map<String, List<PatternRealtimeETA>> {
    return arrivals.groupBy { it.stopId }
}
package pt.carrismetropolitana.mobile.composables.components.transit.pattern_path

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.carrismetropolitana.mobile.R
import pt.carrismetropolitana.mobile.services.cmapi.Facility
import pt.carrismetropolitana.mobile.services.cmapi.PathEntry
import pt.carrismetropolitana.mobile.services.cmapi.RealtimeETA
import pt.carrismetropolitana.mobile.services.cmapi.Stop

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PatternPathItem(
    pathItem: PathEntry,
    expanded: Boolean,
    onClick: () -> Unit,
    onSchedulesButtonClick: () -> Unit,
    onStopDetailsButtonClick: () -> Unit,
    nextArrivals: List<RealtimeETA>
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .clickable { onClick() }
    ) {
        Row {
            Box(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .background(Color.Red)
                    .width(15.dp)
                    .height(30.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(pathItem.stop.name)
                Text(pathItem.stop.municipalityName)

                AnimatedVisibility(visible = expanded) {
                    Column {
                        Text("13:15\t\t13:45\t\t14:15")
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            PatternPathFooterButton(text = "Horários", iconResourceId = R.drawable.phosphoricons_clock) {
                                
                            }
                            PatternPathFooterButton(text = "Sobre a paragem", iconResourceId = R.drawable.phosphoricons_map_pin_simple_area) {

                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PatternPathItemPreview() {
    val previewPathEntry = PathEntry(
        allowDropOff = false,
        allowPickup = false,
        distanceDelta = 0.6906,
        stop = Stop(
            districtId = "11",
            districtName = "Lisboa",
            facilities = listOf(Facility.School),
            id = "170491",
            lat = "38.768486",
            lines = listOf(
                "1209", "1215", "1220", "1222", "1223", "1225", "1233", "1236", "1520",
                "1521", "1523", "1613", "1622", "1731"
            ),
            locality = "Sintra",
            lon = "-9.300971",
            municipalityId = "1111",
            municipalityName = "Sintra",
            name = "R Elias Garcia (Supermercado)",
            operationalStatus = "ACTIVE",
            parishId = null,
            parishName = null,
            patterns = listOf(
                "1209_0_2", "1209_1_2", "1215_0_3", "1220_0_3", "1222_0_3", "1223_0_1",
                "1223_1_1", "1223_2_1", "1225_0_3", "1233_0_3", "1236_0_2", "1520_0_2",
                "1521_0_2", "1523_0_1", "1613_0_1", "1613_1_1", "1622_0_1", "1731_0_1"
            ),
            regionId = "PT170",
            regionName = "AML",
            routes = listOf(
                "1209_0", "1209_1", "1215_0", "1220_0", "1222_0", "1223_0", "1223_1",
                "1223_2", "1225_0", "1233_0", "1236_0", "1520_0", "1521_0", "1523_0",
                "1613_0", "1613_1", "1622_0", "1731_0"
            ),
            shortName = "a definir",
            ttsName = "Rua Elias Garcia ( Supermercado )",
            wheelchairBoarding = "0",
            stopId = "170491"
        ),
        stopSequence = 2
    )

    PatternPathItem(
        pathItem = previewPathEntry,
        expanded = false,
        onClick = { /*TODO*/ },
        onSchedulesButtonClick = { /*TODO*/ },
        onStopDetailsButtonClick = { /*TODO*/ },
        nextArrivals = listOf()
    )
}

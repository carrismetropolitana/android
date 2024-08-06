package pt.carrismetropolitana.mobile.composables.screens.more

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import pt.carrismetropolitana.mobile.services.cmapi.ENCM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ENCMView(
    navController: NavController
) {
    val encms = listOf<ENCM>(
        ENCM(
            id = "1",
            name = "Carris Metropolitana",
            lat = "38.7",
            lon = "-9.0",
            phone = "+351 21 21 21 21",
            email = "info@carrismetropolitana.pt",
            url = "https://www.carrismetropolitana.pt",
            address = "Rua do Carris, 100",
            postalCode = "1000-100",
            locality = "Lisboa",
            parishId = "1",
            parishName = "Lisboa",
            municipalityId = "1",
            municipalityName = "Lisboa",
            districtId = "1",
            districtName = "Lisboa",
            regionId = "1",
            regionName = "Lisboa",
            hoursMonday = listOf("08:00", "12:00"),
            hoursTuesday = listOf("08:00", "12:00"),
            hoursWednesday = listOf("08:00", "12:00"),
            hoursThursday = listOf("08:00", "12:00"),
            hoursFriday = listOf("08:00", "12:00"),
            hoursSaturday = listOf("08:00", "12:00"),
            hoursSunday = listOf("08:00", "12:00"),
            hoursSpecial = "08:00",
            stops = listOf("121212", "131313"),
            currentlyWaiting = 1,
            expectedWaitTime = 1,
            activeCounters = 1,
            isOpen = true
        ),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Espaços navegante®") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            item {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://www.navegante.pt/assets/94954915-432c-4a56-8157-c8b2c94e793d?access_token=utilizador.rest")
                        .crossfade(true)
                        .build(),
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )

                Text(
                    text = "Dedicados ao passageiro, os chamados Espaços navegante® Carris Metropolitana, concentram todos os serviços relacionados com a Carris Metropolitana, possibilitando esclarecer dúvidas, solicitar a emissão de cartões navegante® ou mesmo aderir ao passe navegante família e antigo combatente.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )

                Column {
                    ENCMItem()
                }
            }

//            items(encms) { encm ->
//                ENCMItem(
//                    encm = encm,
//                    locationManager = locationManager,
//                    onNavigateClick = {
//                        selectedENCM = encm
//                        val availableMapApps = getAvailableMapApps(context)
//                        if (availableMapApps.size > 1) {
//                            isMapAppSelectionDialogVisible = true
//                        } else {
//                            navigateTo(encm.lat.toDouble(), encm.lon.toDouble(), availableMapApps[0])
//                        }
//                    }
//                )
//                Divider()
//            }
        }
    }

}

@Composable
fun ENCMItem(
//    encm: ENCM,
//    locationManager: LocationManager,
//    onNavigateClick: () -> Unit
) {
    Row {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
//            Text(encm.name.drop(39))
            Text("Queluz", fontWeight = FontWeight.Black, fontSize = 24.sp)

            Text("Morada", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray)
            Text("Avenida José Elias Garcia 71")
//            Text(encm.address)

            Text("Horário", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray)
            Text("Seg-Sex\n08:00-19:00")
//            Text(encmToHoursOpenString(encm))

            Text("ABERTO", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Green, modifier = Modifier.padding(vertical = 8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("11 em espera")
                Text("20 min")
            }
        }

        // TODO: navicon and distance
    }
}




enum class Weekday(val displayName: String) {
    MONDAY("Seg"),
    TUESDAY("Ter"),
    WEDNESDAY("Qua"),
    THURSDAY("Qui"),
    FRIDAY("Sex"),
    SATURDAY("Sáb"),
    SUNDAY("Dom")
}

data class ENCMTimetableEntry(
    val dayOfTheWeek: Weekday,
    val hourIntervals: List<String>
)

fun encmToHoursOpenString(encm: ENCM): String {
    val timetable = mutableListOf<ENCMTimetableEntry>()

    timetable.add(ENCMTimetableEntry(Weekday.MONDAY, encm.hoursMonday))
    timetable.add(ENCMTimetableEntry(Weekday.TUESDAY, encm.hoursTuesday))
    timetable.add(ENCMTimetableEntry(Weekday.WEDNESDAY, encm.hoursWednesday))
    timetable.add(ENCMTimetableEntry(Weekday.THURSDAY, encm.hoursThursday))
    timetable.add(ENCMTimetableEntry(Weekday.FRIDAY, encm.hoursFriday))
    timetable.add(ENCMTimetableEntry(Weekday.SATURDAY, encm.hoursSaturday))
    timetable.add(ENCMTimetableEntry(Weekday.SUNDAY, encm.hoursSunday))

    val equalToMonday = timetable.filter { it.hourIntervals == encm.hoursMonday }

    var timeIntervals = ""

    for (hourIntervalIdx in encm.hoursMonday.indices) {
        val hourInterval = encm.hoursFriday[hourIntervalIdx]
        val isLast = hourIntervalIdx == encm.hoursMonday.size - 1

        timeIntervals += hourInterval + if (isLast) "" else "\n"
    }

    return "${equalToMonday.first().dayOfTheWeek.name}-${equalToMonday.last().dayOfTheWeek.name}\n$timeIntervals"
}
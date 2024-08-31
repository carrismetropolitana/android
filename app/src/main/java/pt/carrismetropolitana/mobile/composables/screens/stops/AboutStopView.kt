package pt.carrismetropolitana.mobile.composables.screens.stops

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import pt.carrismetropolitana.mobile.LocalStopsManager
import pt.carrismetropolitana.mobile.R
import pt.carrismetropolitana.mobile.composables.FavoriteType
import pt.carrismetropolitana.mobile.composables.components.Pill
import pt.carrismetropolitana.mobile.composables.components.feedback.UserFeedbackForm
import pt.carrismetropolitana.mobile.composables.components.feedback.demoUserFeedbackFormQuestions
import pt.carrismetropolitana.mobile.composables.screens.lines.LineItem
import pt.carrismetropolitana.mobile.composables.screens.lines.SquareButton
import pt.carrismetropolitana.mobile.services.cmapi.CMAPI
import pt.carrismetropolitana.mobile.services.cmapi.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutStopView(
    stopId: String,
    navController: NavController,
    parentPadding: PaddingValues
) {
    val stopsManager = LocalStopsManager.current

    val stops = stopsManager.data.collectAsState().value
    val stop = stops.firstOrNull { it.id == stopId }

    val patternsFromStop = remember { mutableStateListOf<Pattern>() }

    LaunchedEffect(Unit) {
        if (stop == null) return@LaunchedEffect

        for (patternId in stop.patterns!!) {
            val pattern = CMAPI.shared.getPattern(patternId) ?: continue // elvis operator :)
            patternsFromStop += pattern
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Paragem")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
//                        val sendIntent: Intent = Intent(Intent.ACTION_SEND, Uri.parse("https://beta.carrismetropolitana.pt/lines/1523"))
//                        context.startActivity(sendIntent)
                    }) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        }
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .padding(bottom = parentPadding.calculateBottomPadding())
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 12.dp),

                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier.border(
                                    1.dp,
                                    Color.Gray,
                                    shape = RoundedCornerShape(24.dp)
                                )
                            ) {
                                Text(
                                    stop?.id ?: "",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                            }
                            Text(
                                stop?.lat ?: "",
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                            Text(
                                stop?.lon ?: "",
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        }

                        Text(stop?.name ?: "", fontWeight = FontWeight.Bold, fontSize = 24.sp)

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            SquareButton(
                                icon = ImageVector.vectorResource(R.drawable.phosphoricons_star),
                                iconTint = Color("#ffcc00".toColorInt()),
                                iconContentDescription = "Favorite Stop Icon",
                                size = 60
                            ) {
                                navController.navigate("favorite_item_customization/${FavoriteType.STOP.name}/$stopId")
                            }
                            SquareButton(
                                icon = ImageVector.vectorResource(R.drawable.phosphoricons_map_trifold),
                                iconTint = Color.Black,
                                iconContentDescription = "Map", size = 60
                            ) {
                                /* TODO */
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding()
                                .padding(top = 24.dp),
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            Text(
                                "Destinos a partir desta paragem",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }

            if (patternsFromStop.isNotEmpty()) {
                items(patternsFromStop) { pattern ->
                    StopPatternDestinationItem(
                        pattern,
                        isLastInList = false,
                        onClick = { navController.navigate("line_details/${pattern.lineId}?overridePatternId=${pattern.id}") })
                }
            }



            item {
                UserFeedbackForm(
                    title = "Estas informações estão corretas?",
                    description = "Ajude-nos a melhorar os transportes para todos.",
                    questions = demoUserFeedbackFormQuestions
                )
            }
        }
    }
}

@Composable
fun StopPatternDestinationItem(pattern: Pattern, isLastInList: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
            .clickable { onClick() }
    ) {
        Pill(text = pattern.shortName, color = Color(pattern.color.toColorInt()), textColor = Color(pattern.textColor.toColorInt()), size = 60)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = pattern.headsign,
            fontSize = 16.sp, // Set font size in scaled pixels (sp)
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
    }
    if (!isLastInList) {
        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
    }
    Spacer(modifier = Modifier.height(8.dp))
}
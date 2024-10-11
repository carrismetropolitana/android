package pt.carrismetropolitana.mobile.composables.screens.lines

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import pt.carrismetropolitana.mobile.FIRST_TIME_LAUNCH
import pt.carrismetropolitana.mobile.LocalLinesManager
import pt.carrismetropolitana.mobile.dataStore
import pt.carrismetropolitana.mobile.services.cmapi.Line
import pt.carrismetropolitana.mobile.utils.normalizedForSearch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "linesSearch")
val LINE_SEARCH_HISTORY = stringSetPreferencesKey("searchHistory")

fun getLineSearchHistory(context: Context): Flow<Set<String>> {
    return context.dataStore.data.map { preferences ->
        preferences[LINE_SEARCH_HISTORY] ?: emptySet()
    }
}

fun setLineSearchHistory(context: Context, searchHistory: Set<String>) {
    runBlocking {
        context.dataStore.edit { preferences ->
            preferences[LINE_SEARCH_HISTORY] = searchHistory
        }
    }
}

// limited to 5 lines
fun addLineToSearchHistory(context: Context, lineId: String) {
    runBlocking {
        context.dataStore.edit { preferences ->
            val searchHistory = preferences[LINE_SEARCH_HISTORY] ?: emptySet()
            if (searchHistory.size == 5) {
                preferences[LINE_SEARCH_HISTORY] = (searchHistory.drop(1) + lineId).toSet()
            } else {
                preferences[LINE_SEARCH_HISTORY] = searchHistory + lineId
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinesScreen(navController: NavController, parentPaddingValues: PaddingValues) {
    val context = LocalContext.current
    val linesManager = LocalLinesManager.current

    var text by rememberSaveable {
        mutableStateOf("")
    }

    var active by rememberSaveable {
        mutableStateOf(false)
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    var searchFilteredLines by remember { mutableStateOf(listOf<Line>()) }
    val searchHistory = getLineSearchHistory(context).collectAsState(initial = emptySet())

    LaunchedEffect(text) {
        if (text.isNotEmpty()) {
            val normalizedText = text.normalizedForSearch()
            searchFilteredLines = linesManager.data.value.filter {
                it.shortName.contains(normalizedText, true)
                        || it.longNameNormalized.contains(normalizedText, true)
            }
        } else {
            searchFilteredLines = listOf()
        }
    }

    Scaffold(
        topBar = {
            Column {
                AnimatedVisibility(visible = !active) {
                    MediumTopAppBar(
                        title = {
                            Text("Linhas", color = Color.Black, fontWeight = FontWeight.Bold)
                        },
                        colors = TopAppBarDefaults.mediumTopAppBarColors(
                            containerColor = Color("#FFDD01".toColorInt()),
                            scrolledContainerColor = Color("#FFDD01".toColorInt())
                        ),
                        scrollBehavior = scrollBehavior
                    )
                }
                SearchBar(
                    colors = SearchBarDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.background,
                    ),
                    query = text,
                    onQueryChange = {
                        text = it
                    },
                    onSearch = {
                        active = false
                    },
                    active = active,
                    onActiveChange = {
                        active = it
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                    },
                    trailingIcon = {
                        if (active)
                            Icon(
                                modifier = Modifier.clickable {
                                    if (text.isNotEmpty()) {
                                        text = ""
                                    } else {
                                        active = false
                                    }
                                },
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Icon"
                            )
                    },
                    placeholder = {
                        Text(text = "Pesquisar linhas")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color("#FFDD01".toColorInt()))
                        .padding(horizontal = if (active) 0.dp else 12.dp)
                        .padding(bottom = if (active) 0.dp else 12.dp)
                        .padding(top = if (active) parentPaddingValues.calculateTopPadding() else 0.dp),
                    windowInsets = WindowInsets(0, 0, 0, 0)
                ) {
                    if (searchFilteredLines.isEmpty()) {
                            LinesList(
                                lines = linesManager.data.collectAsState().value.filter { line ->
                                    searchHistory.value.contains(line.id)
                                },
                                onLineClick = { lineId ->
                                    navController.navigate("line_details/$lineId")
                                    addLineToSearchHistory(context = context, lineId = lineId)
                                }
                            )
                    } else {
                        LinesList(
                            lines = searchFilteredLines,
                            onLineClick = { lineId -> navController.navigate("line_details/$lineId") })
                    }
                }
            }
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        val lines = linesManager.data.collectAsState().value // check if this keeps updating as state changes
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .padding(bottom = parentPaddingValues.calculateBottomPadding()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (lines.isEmpty()) {
                Text(
                    text = "Loading...",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LinesList(
                    lines = lines,
                    onLineClick = { lineId ->
                        navController.navigate("line_details/$lineId")
                        addLineToSearchHistory(context = context, lineId = lineId)
                    }
                )
            }
        }
    }
}

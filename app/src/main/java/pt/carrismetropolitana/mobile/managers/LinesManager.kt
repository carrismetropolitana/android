package pt.carrismetropolitana.mobile.managers

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.carrismetropolitana.mobile.services.cmapi.CMAPI
import pt.carrismetropolitana.mobile.services.cmapi.Line

//class LinesManager {
//    private val cmAPI = CMAPI.shared
//    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
//
//    val lines = mutableStateOf<List<Line>>(emptyList())
//
//    fun fetchLines() {
//        coroutineScope.launch {
//            val result = withContext(Dispatchers.IO) {
//                cmAPI.getLines()
//            }
//
//            // Update the state on the main thread
//            lines.value = result
//        }
//    }
//}

class LinesManager(fetchIntervalMillis: Long? = 60_000L) : DataManager<Line>(
    apiCall = { CMAPI.shared.getLines() },
     fetchIntervalMillis
)
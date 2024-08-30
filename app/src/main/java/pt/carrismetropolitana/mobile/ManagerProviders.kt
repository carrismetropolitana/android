package pt.carrismetropolitana.mobile

import androidx.compose.runtime.compositionLocalOf
import pt.carrismetropolitana.mobile.managers.LinesManager

val LocalLinesManager = compositionLocalOf<LinesManager> { error("No LinesManager provided") }
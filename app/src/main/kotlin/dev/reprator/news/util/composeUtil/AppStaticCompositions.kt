package dev.reprator.news.util.composeUtil

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalWindowSizeClass = staticCompositionLocalOf<WindowSizeClass> {
  error("No WindowSizeClass available")
}


/**
 * A class to model background color and tonal elevation values for Now in Android.
 */
@Immutable
data class TintTheme(
  val iconTint: Color = Color.Unspecified,
)

/**
 * A composition local for [TintTheme].
 */
val LocalTintTheme = staticCompositionLocalOf { TintTheme() }
package dev.reprator.news.util.composeUtil

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalWindowAdaptiveInfo = staticCompositionLocalOf<WindowAdaptiveInfo> {
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
package dev.reprator.news.util.composeUtil

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector
import dev.reprator.news.R

/**
 * Now in Android icons. Material icons are [ImageVector]s, custom icons are drawable resource IDs.
 */
object AppIcons {
    val BACK = Icons.AutoMirrored.Filled.ArrowBack to R.string.back_button
    val Refresh = Icons.Default.Refresh to R.string.retry
    val CLOCK = Icons.Default.AccessTime to R.string.publistedAt
    val NAV_SELECTED_HOME = Icons.Default.Home to R.string.tab_home
    val NAV_UNSELECTED_HOME = Icons.Outlined.Home to R.string.tab_home
    val NAV_SELECTED_BOOKMARK = Icons.Default.Bookmarks to R.string.tab_bookmarks
    val NAV_UNSELECTED_BOOKMARK = Icons.Outlined.Bookmarks to R.string.tab_bookmarks
}

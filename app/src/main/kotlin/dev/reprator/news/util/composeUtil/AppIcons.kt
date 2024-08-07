package dev.reprator.news.util.composeUtil

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import dev.reprator.news.R

object AppIcons {
    val BACK = Icons.AutoMirrored.Filled.ArrowBack to R.string.back_button
    val Refresh = Icons.Default.Refresh to R.string.retry
    val CLOCK = Icons.Default.AccessTime to R.string.publistedAt
    val NAV_SELECTED_HOME = Icons.Default.Home to R.string.tab_home
    val NAV_UNSELECTED_HOME = Icons.Outlined.Home to R.string.tab_home
    val NAV_SELECTED_BOOKMARK = Icons.Default.Bookmarks to R.string.tab_bookmarks
    val NAV_UNSELECTED_BOOKMARK = Icons.Outlined.Bookmarks to R.string.tab_bookmarks
    val NAV_MENU_OPEN = Icons.AutoMirrored.Filled.MenuOpen to R.string.close_drawer
    val NAV_MENU_CLOSED = Icons.Default.Menu to R.string.navigation_drawer
}

package dev.reprator.news.util.composeUtil.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import dev.reprator.news.presentation.bookmarks.navigation.ROUTE_BOOKMARK
import dev.reprator.news.presentation.newsList.navigation.ROUTE_NEWS
import dev.reprator.news.util.composeUtil.AppIcons.NAV_SELECTED_BOOKMARK
import dev.reprator.news.util.composeUtil.AppIcons.NAV_SELECTED_HOME
import dev.reprator.news.util.composeUtil.AppIcons.NAV_UNSELECTED_BOOKMARK
import dev.reprator.news.util.composeUtil.AppIcons.NAV_UNSELECTED_HOME

data class NewsTopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int
)

class NewsNavigationActions(private val navController: NavHostController) {

    fun navigateTo(destination: NewsTopLevelDestination) {
        navController.navigate(destination.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

val TOP_LEVEL_DESTINATIONS = listOf(
    NewsTopLevelDestination(
        route = ROUTE_NEWS,
        selectedIcon = NAV_SELECTED_HOME.first,
        unselectedIcon = NAV_UNSELECTED_HOME.first,
        iconTextId = NAV_SELECTED_HOME.second
    ),

    NewsTopLevelDestination(
        route = ROUTE_BOOKMARK,
        selectedIcon = NAV_SELECTED_BOOKMARK.first,
        unselectedIcon = NAV_UNSELECTED_BOOKMARK.first,
        iconTextId = NAV_SELECTED_BOOKMARK.second
    )
)

fun NewsTopLevelDestination.selectedIcon(selectedDestination: String): ImageVector{
    val isSelected = selectedDestination == route
    return if(isSelected) selectedIcon else unselectedIcon
}

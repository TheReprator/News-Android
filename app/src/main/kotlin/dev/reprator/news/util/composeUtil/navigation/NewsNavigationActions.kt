package dev.reprator.news.ui.composeUtil.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import dev.reprator.news.R
import dev.reprator.news.ui.bookmarks.navigation.ROUTE_BOOKMARK
import dev.reprator.news.ui.newsDetail.navigation.navigateToNewsDetail
import dev.reprator.news.ui.newsList.navigation.ROUTE_NEWS

data class NewsTopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int
)

class NewsNavigationActions(private val navController: NavHostController) {

    fun navigateTo(destination: NewsTopLevelDestination) {
        navController.navigate(destination.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    fun navigateToDetail() = navController.navigateToNewsDetail()
}

val TOP_LEVEL_DESTINATIONS = listOf(
    NewsTopLevelDestination(
        route = ROUTE_NEWS,
        selectedIcon = Icons.Default.Home,
        unselectedIcon = Icons.Outlined.Home,
        iconTextId = R.string.tab_home
    ),

    NewsTopLevelDestination(
        route = ROUTE_BOOKMARK,
        selectedIcon = Icons.Default.Bookmarks,
        unselectedIcon = Icons.Outlined.Bookmarks,
        iconTextId = R.string.tab_bookmarks
    )

)

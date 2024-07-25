package dev.reprator.news.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.reprator.news.presentation.bookmarks.navigation.bookmarkScreen
import dev.reprator.news.presentation.newsListDetail.navigation.ROUTE_NEWS_DETAIL
import dev.reprator.news.presentation.newsListDetail.navigation.newsListDetailScreen
import dev.reprator.news.util.composeUtil.navigation.NewsNavigationActions
import dev.reprator.news.util.composeUtil.navigation.NewsNavigationWrapper

@Composable
fun NewsApp() {

    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        NewsNavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = navBackStackEntry?.destination?.route ?: ROUTE_NEWS_DETAIL

    val snackBarHostState = remember { SnackbarHostState() }

    Surface {
        NewsNavigationWrapper(
            snackBarHostState = snackBarHostState,
            selectedDestination = selectedDestination,
            navigateToTopLevelDestination = navigationActions::navigateTo
        ) {
            NewsNavHost(navController = navController)
        }
    }
}


@Composable
private fun NewsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = ROUTE_NEWS_DETAIL,
    ) {
        newsListDetailScreen()
        bookmarkScreen()
    }
}
package dev.reprator.news.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.reprator.news.ui.bookmarks.navigation.bookmarkScreen
import dev.reprator.news.util.composeUtil.navigation.NewsNavigationActions
import dev.reprator.news.util.composeUtil.navigation.NewsNavigationWrapper
import dev.reprator.news.ui.newsDetail.navigation.navigateToNewsDetail
import dev.reprator.news.ui.newsDetail.navigation.newsDetailScreen
import dev.reprator.news.ui.newsList.navigation.ROUTE_NEWS
import dev.reprator.news.ui.newsList.navigation.newsScreen
import timber.log.Timber

@Composable
fun NewsApp() {

    val snackBarHostState = remember { SnackbarHostState() }

    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        NewsNavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = navBackStackEntry?.destination?.route ?: ROUTE_NEWS

    Surface {
        NewsNavigationWrapper(
            snackbarHostState = snackBarHostState,
            selectedDestination = selectedDestination,
            navigateToTopLevelDestination = navigationActions::navigateTo
        ) {
            NewsNavHost(
                navController = navController,
                onShowSnackBar = { message ->
                   Timber.e("Toast Message:  $message")
                },
            )
        }
    }
}


@Composable
private fun NewsNavHost(
    navController: NavHostController,
    onShowSnackBar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = ROUTE_NEWS,
    ) {
        newsScreen(onClick = {
            navController.navigateToNewsDetail()
        }, onShowSnackBar)

        bookmarkScreen(onClick = {
            navController.navigateToNewsDetail()
        }, onShowSnackBar)

        newsDetailScreen({}, onShowSnackBar)

    }
}
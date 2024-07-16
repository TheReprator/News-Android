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
import dev.reprator.news.presentation.newsDetail.navigation.navigateToNewsDetail
import dev.reprator.news.presentation.newsDetail.navigation.newsDetailScreen
import dev.reprator.news.presentation.newsList.navigation.ROUTE_NEWS
import dev.reprator.news.presentation.newsList.navigation.newsScreen
import dev.reprator.news.util.composeUtil.navigation.NewsNavigationActions
import dev.reprator.news.util.composeUtil.navigation.NewsNavigationWrapper
import kotlinx.coroutines.launch

@Composable
fun NewsApp() {

    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        NewsNavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = navBackStackEntry?.destination?.route ?: ROUTE_NEWS

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    Surface {
        NewsNavigationWrapper(
            snackBarHostState = snackBarHostState,
            selectedDestination = selectedDestination,
            navigateToTopLevelDestination = navigationActions::navigateTo
        ) {
            NewsNavHost(navController = navController, snackBarHostState, {
                scope.launch {
                    snackBarHostState.showSnackbar(it)
                }
            })
        }
    }
}


@Composable
private fun NewsNavHost(
    navController: NavHostController,
    snackBarHostState: SnackbarHostState,
    showToast: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = ROUTE_NEWS,
    ) {
        newsScreen(onClick = {
            snackBarHostState.dismissSnackBar()
            navController.navigateToNewsDetail(it)
        }, showToast, snackBarHostState)

        bookmarkScreen(onClick = {
            snackBarHostState.dismissSnackBar()
            navController.navigateToNewsDetail(it)
        })

        newsDetailScreen {
            navController.popBackStack()
        }

    }
}

fun SnackbarHostState.dismissSnackBar() {
    val state = currentSnackbarData
    state?.dismiss()
}
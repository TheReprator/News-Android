package dev.reprator.news.presentation

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
import dev.reprator.news.presentation.bookmarks.navigation.bookmarkScreen
import dev.reprator.news.presentation.home.navigation.ROUTE_HOME
import dev.reprator.news.presentation.home.navigation.homeScreen
import dev.reprator.news.util.composeUtil.navigation.NewsNavigationActions
import dev.reprator.news.util.composeUtil.navigation.NewsNavigationWrapper

@Composable
fun NewsApp() {

    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        NewsNavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = navBackStackEntry?.destination?.route ?: ROUTE_HOME

    val snackBarHostState = remember { SnackbarHostState() }

    Surface {
        NewsNavigationWrapper(
            snackBarHostState = snackBarHostState,
            selectedDestination = selectedDestination,
            navigateToTopLevelDestination = navigationActions::navigateTo,
        content= { modifier ->
            NewsNavHost(navController = navController, modifier = modifier)
        })
    }
}


@Composable
private fun NewsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = Modifier.then(modifier),
        navController = navController,
        startDestination = ROUTE_HOME,
    ) {
        homeScreen()
        bookmarkScreen()
    }
}
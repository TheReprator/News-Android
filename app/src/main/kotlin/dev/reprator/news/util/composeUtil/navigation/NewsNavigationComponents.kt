package dev.reprator.news.util.composeUtil.navigation

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import dev.reprator.news.presentation.newsList.navigation.ROUTE_NEWS
import dev.reprator.news.util.composeUtil.LocalWindowSizeClass
import dev.reprator.news.util.composeUtil.theme.ContrastAwareNewsTheme


internal enum class NavigationType {
    BOTTOM_NAVIGATION,
    RAIL,
    PERMANENT_DRAWER;

    companion object {
        fun forWindowSizeSize(windowSizeClass: WindowSizeClass): NavigationType = when {
            windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact -> BOTTOM_NAVIGATION
            windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact -> BOTTOM_NAVIGATION
            windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium -> RAIL
            else -> PERMANENT_DRAWER
        }
    }
}

@Composable
fun NewsNavigationWrapper(
    snackBarHostState: SnackbarHostState,
    selectedDestination: String,
    navigateToTopLevelDestination: (NewsTopLevelDestination) -> Unit,
    content: @Composable PaddingValues.() -> Unit
) {

    val windowSizeClass = LocalWindowSizeClass.current
    val navigationType = remember(windowSizeClass) {
        NavigationType.forWindowSizeSize(windowSizeClass)
    }

    Scaffold(bottomBar = {
        if (navigationType == NavigationType.BOTTOM_NAVIGATION) {
            Box {
                HomeNavigationBar(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = navigateToTopLevelDestination,
                )
            }
        }
    }, snackbarHost = { SnackbarHost(snackBarHostState) }) { contentPadding ->

        Row(modifier = Modifier.fillMaxSize().padding(contentPadding)) {

            if (navigationType == NavigationType.RAIL) {
                HomeNavigationRail(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = navigateToTopLevelDestination,
                )
            } else if (navigationType == NavigationType.PERMANENT_DRAWER) {
                HomeNavigationDrawer(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = navigateToTopLevelDestination,
                    modifier = Modifier.fillMaxHeight(),
                )
            }

            content(contentPadding)
        }
    }
}


@Composable
private fun HomeNavigationDrawer(
    selectedDestination: String,
    navigateToTopLevelDestination: (NewsTopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.safeContent)
            .padding(16.dp)
            .widthIn(max = 280.dp),
    ) {
        TOP_LEVEL_DESTINATIONS.forEach { destination ->
            NavigationDrawerItem(
                selected = selectedDestination == destination.route,
                label = {
                    Text(
                        text = stringResource(id = destination.iconTextId),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                },
                icon = {
                    Icon(
                        imageVector = destination.selectedIcon(selectedDestination),
                        contentDescription = stringResource(
                            id = destination.iconTextId
                        )
                    )
                },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent
                ),
                onClick = { navigateToTopLevelDestination(destination) }
            )
        }
    }
}


@Composable
private fun HomeNavigationRail(
    selectedDestination: String,
    navigateToTopLevelDestination: (NewsTopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationRail(modifier = modifier) {
        Spacer(Modifier.weight(1f))
        TOP_LEVEL_DESTINATIONS.forEach { destination ->
            NavigationRailItem(
                selected = selectedDestination == destination.route,
                onClick = { navigateToTopLevelDestination(destination) },
                icon = {
                    Icon(
                        imageVector = destination.selectedIcon(selectedDestination),
                        contentDescription = stringResource(
                            id = destination.iconTextId
                        )
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = destination.iconTextId),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                },
            )
        }
        Spacer(Modifier.weight(1f))
    }
}


@Composable
fun HomeNavigationBar(
    selectedDestination: String,
    navigateToTopLevelDestination: (NewsTopLevelDestination) -> Unit
) {
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        TOP_LEVEL_DESTINATIONS.forEach { destination ->
            NavigationBarItem(
                selected = selectedDestination == destination.route,
                onClick = { navigateToTopLevelDestination(destination) },
                icon = {
                    Icon(
                        imageVector = destination.selectedIcon(selectedDestination),
                        contentDescription = stringResource(id = destination.iconTextId)
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = destination.iconTextId),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                },
            )
        }
    }
}


@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NewsNavigationDrawerPreview() {
    val navController = rememberNavController()
    val navigationActions = NewsNavigationActions(navController)

    ContrastAwareNewsTheme {
        HomeNavigationDrawer(ROUTE_NEWS, navigationActions::navigateTo)
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NewsNavigationRailPreview() {
    val navController = rememberNavController()
    val navigationActions = NewsNavigationActions(navController)

    ContrastAwareNewsTheme {
        HomeNavigationRail(ROUTE_NEWS, navigationActions::navigateTo)
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NewsNavigationBottomPreview() {
    val navController = rememberNavController()
    val navigationActions = NewsNavigationActions(navController)

    ContrastAwareNewsTheme {
        HomeNavigationBar(ROUTE_NEWS, navigationActions::navigateTo)
    }
}
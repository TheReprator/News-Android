package dev.reprator.news.ui.composeUtil.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.reprator.news.ui.composeUtil.LocalWindowSizeClass


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
    snackbarHostState: SnackbarHostState,
    selectedDestination: String,
    navigateToTopLevelDestination: (NewsTopLevelDestination) -> Unit,
    content: @Composable PaddingValues.() -> Unit
) {

    val windowSizeClass = LocalWindowSizeClass.current
    val navigationType = remember(windowSizeClass) {
        NavigationType.forWindowSizeSize(windowSizeClass)
    }

    Scaffold (bottomBar={
        if (navigationType == NavigationType.BOTTOM_NAVIGATION) {
            Box {
                HomeNavigationBar (
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination= navigateToTopLevelDestination,
                )
            }
        }
    }, snackbarHost = { SnackbarHost(snackbarHostState) }){ contentPadding ->
        Box(Modifier.fillMaxSize()) {
            if (navigationType == NavigationType.RAIL) {
                HomeNavigationRail(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination= navigateToTopLevelDestination,
                )

                VerticalDivider()
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
        TOP_LEVEL_DESTINATIONS.forEach { replyDestination ->
            NavigationDrawerItem(
                selected = selectedDestination == replyDestination.route,
                label = {
                    Text(
                        text = stringResource(id = replyDestination.iconTextId),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                },
                icon = {
                    Icon(
                        imageVector = replyDestination.selectedIcon,
                        contentDescription = stringResource(
                            id = replyDestination.iconTextId
                        )
                    )
                },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent
                ),
                onClick = { navigateToTopLevelDestination(replyDestination) }
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
        TOP_LEVEL_DESTINATIONS.forEach { replyDestination ->
            NavigationRailItem(
                selected = selectedDestination == replyDestination.route,
                onClick = { navigateToTopLevelDestination(replyDestination) },
                icon = {
                    Icon(
                        imageVector = replyDestination.selectedIcon,
                        contentDescription = stringResource(
                            id = replyDestination.iconTextId
                        )
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = replyDestination.iconTextId),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                },
            )
        }
    }
}


@Composable
fun HomeNavigationBar(
    selectedDestination: String,
    navigateToTopLevelDestination: (NewsTopLevelDestination) -> Unit
) {
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        TOP_LEVEL_DESTINATIONS.forEach { replyDestination ->
            NavigationBarItem(
                selected = selectedDestination == replyDestination.route,
                onClick = { navigateToTopLevelDestination(replyDestination) },
                icon = {
                    Icon(
                        imageVector = replyDestination.selectedIcon,
                        contentDescription = stringResource(id = replyDestination.iconTextId)
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = replyDestination.iconTextId),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                },
            )
        }
    }
}
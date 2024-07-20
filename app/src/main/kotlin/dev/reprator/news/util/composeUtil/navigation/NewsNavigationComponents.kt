package dev.reprator.news.util.composeUtil.navigation

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import dev.reprator.news.R
import dev.reprator.news.presentation.newsListDetail.navigation.ROUTE_NEWS_DETAIL
import dev.reprator.news.util.composeUtil.AppIcons
import dev.reprator.news.util.composeUtil.LocalWindowAdaptiveInfo
import dev.reprator.news.util.composeUtil.theme.ContrastAwareNewsTheme
import kotlinx.coroutines.launch

@Composable
fun NewsNavigationWrapper(
    snackBarHostState: SnackbarHostState,
    selectedDestination: String,
    navigateToTopLevelDestination: (NewsTopLevelDestination) -> Unit,
    content: @Composable () -> Unit
) {

    val windowAdaptiveInfo = LocalWindowAdaptiveInfo.current
    val windowSize = with(LocalDensity.current) {
        currentWindowSize().toSize().toDpSize()
    }

    val navLayoutType = when {
        windowAdaptiveInfo.windowPosture.isTabletop -> NavigationSuiteType.NavigationBar
        windowAdaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT -> NavigationSuiteType.NavigationBar
        windowAdaptiveInfo.windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT -> NavigationSuiteType.NavigationRail
        windowAdaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED &&
                windowSize.width >= 1200.dp -> NavigationSuiteType.NavigationDrawer

        else -> NavigationSuiteType.NavigationRail
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val gesturesEnabled =
        drawerState.isOpen || navLayoutType == NavigationSuiteType.NavigationRail

    BackHandler(enabled = drawerState.isOpen) {
        coroutineScope.launch {
            drawerState.close()
        }
    }

    ModalNavigationDrawer(drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        drawerContent = {
            ModalNavigationDrawerContent(
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigateToTopLevelDestination
            )
        }) {

        NavigationSuiteScaffoldLayout(layoutType = navLayoutType, navigationSuite = {
            when (navLayoutType) {
                NavigationSuiteType.NavigationBar -> {
                    HomeNavigationBar(
                        selectedDestination = selectedDestination,
                        navigateToTopLevelDestination = navigateToTopLevelDestination,
                    )
                }

                NavigationSuiteType.NavigationRail -> {

                    HomeNavigationRail(
                        selectedDestination = selectedDestination,
                        navigateToTopLevelDestination = navigateToTopLevelDestination,

                        onDrawerClicked = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        })
                }

                NavigationSuiteType.NavigationDrawer -> {
                    HomePermanentNavigationDrawer(
                        selectedDestination = selectedDestination,
                        navigateToTopLevelDestination = navigateToTopLevelDestination,
                        modifier = Modifier.fillMaxHeight(),
                    )
                }
            }
        }) {
            content()
        }
    }
}

@Composable
fun ModalNavigationDrawerContent(
    selectedDestination: String,
    navigateToTopLevelDestination: (NewsTopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
    onDrawerClicked: () -> Unit = {}
) {
    ModalDrawerSheet {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = modifier
                .windowInsetsPadding(WindowInsets.safeContent)
                .widthIn(max = 280.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(id = R.string.app_name).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                IconButton(onClick = onDrawerClicked) {
                    Icon(
                        imageVector = AppIcons.NAV_MENU_OPEN.first,
                        contentDescription = stringResource(id = AppIcons.NAV_MENU_OPEN.second)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
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
}


@Composable
private fun HomePermanentNavigationDrawer(
    selectedDestination: String,
    navigateToTopLevelDestination: (NewsTopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    PermanentDrawerSheet(
        modifier = Modifier.sizeIn(minWidth = 200.dp, maxWidth = 300.dp),
        drawerContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
    ) {
        Column(
            modifier = modifier
                .windowInsetsPadding(WindowInsets.safeContent)
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(16.dp),
                text = stringResource(id = R.string.app_name).uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
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
    }
}


@Composable
private fun HomeNavigationRail(
    selectedDestination: String,
    navigateToTopLevelDestination: (NewsTopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
    onDrawerClicked: () -> Unit = {},
) {
    NavigationRail(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.inverseOnSurface
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            NavigationRailItem(
                selected = false,
                onClick = onDrawerClicked,
                icon = {
                    Icon(
                        imageVector = AppIcons.NAV_MENU_CLOSED.first,
                        contentDescription = stringResource(id = AppIcons.NAV_MENU_CLOSED.second)
                    )
                }
            )
        }

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
    navigateToTopLevelDestination: (NewsTopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
        //.windowInsetsPadding(WindowInsets.navigationBars)
    ) {
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
        HomePermanentNavigationDrawer(ROUTE_NEWS_DETAIL, navigationActions::navigateTo)
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NewsNavigationRailPreview() {
    val navController = rememberNavController()
    val navigationActions = NewsNavigationActions(navController)

    ContrastAwareNewsTheme {
        HomeNavigationRail(ROUTE_NEWS_DETAIL, navigationActions::navigateTo)
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NewsNavigationBottomPreview() {
    val navController = rememberNavController()
    val navigationActions = NewsNavigationActions(navController)

    ContrastAwareNewsTheme {
        HomeNavigationBar(ROUTE_NEWS_DETAIL, navigationActions::navigateTo)
    }
}
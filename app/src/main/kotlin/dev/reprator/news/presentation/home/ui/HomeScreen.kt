package dev.reprator.news.presentation.home.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldDestinationItem
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import dev.reprator.news.R
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.presentation.home.HomeViewModel
import dev.reprator.news.presentation.newsPane.newsDetail.NewsDetailScreen
import dev.reprator.news.util.composeUtil.LocalWindowAdaptiveInfo
import dev.reprator.news.util.composeUtil.isDetailPaneVisible
import dev.reprator.news.util.composeUtil.isListPaneVisible

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val paginatedNewsList = viewModel.paginatedNews.collectAsLazyPagingItems()
    val selectedNews by viewModel.selectedNews.collectAsStateWithLifecycle()

    HomePaneScreen(
        paginatedNewsList, selectedNews = selectedNews, viewModel::setPrimaryCategory,
        viewModel::onNewsClick, viewModel::updateBookMarks
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun HomePaneScreen(
    paginatedNewsList: LazyPagingItems<ModalNews>,
    selectedNews: ModalNews?,
    onCategoryClick: (String) -> Unit,
    onNewsClick: (ModalNews) -> Unit,
    updateBookMark: () -> Unit,
) {
    val listDetailNavigator = rememberListDetailPaneScaffoldNavigator(
        scaffoldDirective = calculatePaneScaffoldDirective(LocalWindowAdaptiveInfo.current),

        initialDestinationHistory = listOfNotNull(
            ThreePaneScaffoldDestinationItem(ListDetailPaneScaffoldRole.List),
            ThreePaneScaffoldDestinationItem<ModalNews>(ListDetailPaneScaffoldRole.Detail).takeIf {
                selectedNews != null
            },
        ),
    )

    BackHandler(listDetailNavigator.canNavigateBack()) {
        listDetailNavigator.navigateBack()
    }

    fun onNewsClickShowDetailPane(selectedNews: ModalNews) {
        onNewsClick(selectedNews)
        listDetailNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail, selectedNews)
    }

    ListDetailPaneScaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface),
        value = listDetailNavigator.scaffoldValue,
        directive = listDetailNavigator.scaffoldDirective,
        listPane = {
            AnimatedPane {
                HomeListPane(
                    paginatedNewsList, onCategoryClick,
                    onNewsClick = ::onNewsClickShowDetailPane, selectedNews = selectedNews,
                    highlightSelectedNews = listDetailNavigator.isDetailPaneVisible()
                )
            }
        },
        detailPane = {
            if (null == selectedNews)
                null
            else
                AnimatedPane {
                    NewsDetailScreen(selectedNews, !listDetailNavigator.isListPaneVisible(), {
                        if (listDetailNavigator.canNavigateBack()) {
                            listDetailNavigator.navigateBack()
                        }
                    }, {
                        updateBookMark()
                    })
                }

        },
    )
}


@Composable
internal fun HomeListPane(
    paginatedNewsList: LazyPagingItems<ModalNews>,
    onCategoryClick: (String) -> Unit,
    onNewsClick: (ModalNews) -> Unit,
    modifier: Modifier = Modifier,
    selectedNews: ModalNews? = null,
    highlightSelectedNews: Boolean = false,
) {
    val categoryArray = stringArrayResource(R.array.news_category)
    val pagerState = rememberPagerState(pageCount = {
        categoryArray.size
    })

    val lazyListState = rememberLazyListState()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { index ->
            onCategoryClick(categoryArray[index])
        }
    }

    Column(modifier = modifier.windowInsetsPadding(WindowInsets.statusBars)) {
        NewsCategoryTab(categoryArray.toList(), pagerState)
        NewsHorizontalPagerList(pagerState, paginatedNewsList, {
            onNewsClick(it)
        }, {
            paginatedNewsList.refresh()
        }, {
            paginatedNewsList.retry()
        }, selectedNews = selectedNews, highlightSelectedNews = highlightSelectedNews,
            listState = lazyListState
        )

    }
}
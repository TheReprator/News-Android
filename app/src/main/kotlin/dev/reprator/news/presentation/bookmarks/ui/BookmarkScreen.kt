package dev.reprator.news.presentation.bookmarks.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldDestinationItem
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.presentation.bookmarks.BookViewModel
import dev.reprator.news.presentation.newsPane.newsDetail.NewsDetailScreen
import dev.reprator.news.presentation.newsPane.newsList.NewsList
import dev.reprator.news.util.composeUtil.LocalWindowAdaptiveInfo
import dev.reprator.news.util.composeUtil.isDetailPaneVisible
import dev.reprator.news.util.composeUtil.isEmpty
import dev.reprator.news.util.composeUtil.isListPaneVisible

@Composable
internal fun BookMarkScreen(
    modifier: Modifier = Modifier,
    viewModel: BookViewModel = hiltViewModel(),
) {
    val paginatedNewsList = viewModel.news.collectAsLazyPagingItems()
    val selectedNews by viewModel.selectedNews.collectAsStateWithLifecycle()

    val lazyListState = rememberLazyListState()

    BookMarkPaneScreen( paginatedNewsList,  viewModel::onNewsClick, selectedNews = selectedNews,
        updateBookMark = viewModel::updateBookMarks, lazyListState = lazyListState,
        modifier = modifier.windowInsetsPadding(WindowInsets.statusBars))
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun BookMarkPaneScreen(
    paginatedNewsList: LazyPagingItems<ModalNews>,
    onNewsClick: (ModalNews) -> Unit,
    updateBookMark: () -> Unit,
    modifier: Modifier = Modifier,
    selectedNews: ModalNews ?= null,
    lazyListState: LazyListState = rememberLazyListState()
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

    val navigateBack = {
        listDetailNavigator.navigateBack()
    }

    BackHandler(listDetailNavigator.canNavigateBack()) {
        navigateBack()
    }

    fun onNewsClickShowDetailPane(selectedNews: ModalNews) {
        onNewsClick(selectedNews)
        listDetailNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail, selectedNews)
    }

    ListDetailPaneScaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface),
        value = listDetailNavigator.scaffoldValue,
        directive = listDetailNavigator.scaffoldDirective,
        listPane = {
            AnimatedPane {
                NewsList(paginatedNewsList, ::onNewsClickShowDetailPane, selectedNews = selectedNews,
                    highlightSelectedNews = listDetailNavigator.isDetailPaneVisible(), lazyListState = lazyListState)
            }
        },
        detailPane = {

            if ((paginatedNewsList.isEmpty) || (null == selectedNews)) {
                if (listDetailNavigator.canNavigateBack()) {
                    navigateBack()
                }
                null
            } else
                AnimatedPane {
                    NewsDetailScreen(selectedNews, !listDetailNavigator.isListPaneVisible(), {
                        navigateBack()
                    }, {
                        updateBookMark()
                    })
                }

        },
    )
}
package dev.reprator.news.presentation.newsList.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import dev.reprator.news.R
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.util.composeUtil.ui.AppViewError
import dev.reprator.news.util.composeUtil.ui.AppViewLoader
import dev.reprator.news.util.composeUtil.ui.EmptyScreen
import kotlinx.coroutines.launch

val <T : Any> LazyPagingItems<T>.isEmpty
    get() = loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && itemCount == 0


@Composable
fun NewsCategoryTab(
    tabList: List<String>,
    pagerState: PagerState = rememberPagerState(pageCount = { tabList.size })
) {
    val coroutineScope = rememberCoroutineScope()
    val selectedIndex = pagerState.currentPage

    ScrollableTabRow(
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        selectedTabIndex = selectedIndex,
        divider = {
            Spacer(modifier = Modifier.height(5.dp))
        },
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                height = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
        },
        edgePadding = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()

    ) {
        tabList.forEachIndexed { index, title ->
            Tab(selected = pagerState.currentPage == index, onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }, text = {
                Text(text = title)
            })
        }
    }
}

@Composable
fun NewsHorizontalPagerContent(
    pagerState: PagerState,
    news: LazyPagingItems<ModalNews>,
    newsClick: (ModalNews) -> Unit,
    reload: () -> Unit,
    showToast: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedNews: ModalNews ?= null,
    highlightSelectedNews: Boolean = false,
) {
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 20.dp), pageSpacing = 10.dp
    ) {
        val lazyListState = rememberLazyListState()

        LazyColumn(
            state = lazyListState,
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(count = news.itemCount, key = news.itemKey { item ->
                item.uniqueKey
            }) { index ->

                news[index]?.let {
                    val isSelectedItem = highlightSelectedNews && selectedNews == it
                    NewsCard(article = it, onClick = newsClick, isSelected = isSelectedItem)
                }
            }

            val loadState = news.loadState.mediator
            item {
                when {
                    (LoadState.Loading == loadState?.refresh) -> {
                        ShimmerEffect()
                    }

                    (LoadState.Loading == loadState?.append) -> {
                        AppViewLoader(Modifier.fillMaxWidth())
                    }

                    (true == loadState?.hasError) -> {
                        val error = stringResource(R.string.newslist_text_generic_error).format(
                            if (loadState.append is LoadState.Error)
                                (loadState.append as LoadState.Error).error
                            else
                                (loadState.refresh as LoadState.Error).error
                        )

                        val isPaginatingError =
                            (loadState.append is LoadState.Error) || news.itemCount > 1

                        if (!isPaginatingError) {
                            AppViewError(
                                error,
                                onReload = reload,
                                modifier = Modifier.fillParentMaxSize()
                            )
                        } else {
                            showToast(error)
                        }
                    }

                    (news.isEmpty) -> {
                        EmptyScreen("No data found")
                    }
                }
            }
        }
    }
}
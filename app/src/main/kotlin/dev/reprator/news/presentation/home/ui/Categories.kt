package dev.reprator.news.presentation.home.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.presentation.newsPane.newsList.NewsList
import kotlinx.coroutines.launch

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
fun NewsHorizontalPagerList(
    pagerState: PagerState,
    news: LazyPagingItems<ModalNews>,
    newsClick: (ModalNews) -> Unit,
    reload: () -> Unit,
    showToast: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedNews: ModalNews? = null,
    highlightSelectedNews: Boolean = false,
    listState: LazyListState = rememberLazyListState()
) {
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 20.dp), pageSpacing = 10.dp
    ) {
        NewsList(news, newsClick, reload, showToast, modifier, listState, selectedNews ,highlightSelectedNews)
    }
}
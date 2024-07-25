package dev.reprator.news.presentation.newsList.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import dev.reprator.news.R
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.presentation.newsList.NewsListViewModel
import dev.reprator.news.util.composeUtil.theme.Dimens.MediumPadding1


@Composable
internal fun NewsListScreen(
    onNewsClick: (ModalNews) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NewsListViewModel = hiltViewModel(),
    highlightSelectedNews: Boolean = false,
) {
    val paginatedNewsList = viewModel.paginatedNews.collectAsLazyPagingItems()
    val selectedNews = viewModel.selectedNews.collectAsState()

    val categoryArray = stringArrayResource(R.array.news_category)
    val pagerState = rememberPagerState(pageCount = {
        categoryArray.size
    })

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { index ->
            //snackBarHostState.dismissSnackBar()
            viewModel.setPrimaryCategory(categoryArray[index])
        }
    }

    Column(modifier = modifier.windowInsetsPadding(WindowInsets.statusBars)) {
        NewsCategoryTab(categoryArray.toList(), pagerState)
        NewsHorizontalPagerContent(pagerState, paginatedNewsList, {
            onNewsClick(it)
            viewModel.onNewsClick(it)
        }, {
            paginatedNewsList.refresh()
        }, {
            //showToast(it)
            paginatedNewsList.retry()
        }, selectedNews = selectedNews.value, highlightSelectedNews = highlightSelectedNews)

    }
}

@Composable
fun ShimmerEffect() {
    Column(verticalArrangement = Arrangement.spacedBy(MediumPadding1)) {
        repeat(10) {
            NewsCardShimmerEffect(
                modifier = Modifier.padding(horizontal = MediumPadding1)
            )
        }
    }
}
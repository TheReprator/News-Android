package dev.reprator.news.ui.newsList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import dev.reprator.news.R
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.ui.newsList.ui.NewsCard
import dev.reprator.news.util.composeUtil.theme.Dimens.MediumPadding1
import dev.reprator.news.util.composeUtil.ui.AppViewError
import dev.reprator.news.util.composeUtil.ui.AppViewLoader
import dev.reprator.news.util.composeUtil.ui.EmptyScreen

private val <T : Any> LazyPagingItems<T>.isEmpty
    get() = loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && itemCount == 0

@Composable
internal fun NewsListScreen(
    onNewsClick: (ModalNews) -> Unit,
    showToast: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NewsListViewModel = hiltViewModel(),
) {
    val paginatedData = viewModel.news.collectAsLazyPagingItems()
    NewsContainer(paginatedData, onNewsClick, showToast) {
        paginatedData.refresh()
    }
}

@Composable
fun NewsContainer(
    news: LazyPagingItems<ModalNews>, newsClick: (ModalNews) -> Unit,
    showToast: (String) -> Unit,
    reload: () -> Unit
) {
    val lazyListState = rememberLazyListState()
    val loadState = news.loadState.mediator

    LazyColumn(state = lazyListState,  contentPadding = PaddingValues(all = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {

        items(count = news.itemCount,  key = news.itemKey { item ->
            item.uniqueKey
        }) { index ->

            news[index]?.let {
                NewsCard(article = it, onClick = newsClick)
            }
        }

        item {
            when {
                (loadState?.refresh == LoadState.Loading) -> {
                    ShimmerEffect()
                }

                (loadState?.append == LoadState.Loading) -> {
                    AppViewLoader(Modifier.fillMaxWidth())
                }

                (loadState?.refresh is LoadState.Error || loadState?.append is LoadState.Error) -> {
                    val isPaginatingError = (loadState.append is LoadState.Error) || news.itemCount > 1

                    val error = stringResource(R.string.newslist_text_generic_error).format(
                        if (loadState.append is LoadState.Error)
                            (loadState.append as LoadState.Error).error
                        else
                            (loadState.refresh as LoadState.Error).error
                    )

                    if (!isPaginatingError) {
                        AppViewError(error,  onReload = reload, modifier = Modifier.fillParentMaxSize())
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
package dev.reprator.news.presentation.bookmarks


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import dev.reprator.news.presentation.newsList.ui.NewsCard
import dev.reprator.news.presentation.newsList.ui.ShimmerEffect
import dev.reprator.news.presentation.newsList.ui.isEmpty
import dev.reprator.news.util.composeUtil.ui.AppViewError
import dev.reprator.news.util.composeUtil.ui.AppViewLoader
import dev.reprator.news.util.composeUtil.ui.EmptyScreen

@Composable
internal fun BookMarkScreen(
    modifier: Modifier = Modifier,
    viewModel: BookViewModel = hiltViewModel(),
) {
    val paginatedNewsList = viewModel.news.collectAsLazyPagingItems()

    val lazyListState = rememberLazyListState()
    BookMarkList(lazyListState, paginatedNewsList, {})
}

@Composable
fun BookMarkList(listState: LazyListState,
                 news: LazyPagingItems<ModalNews>,
                 onNewsClick: (ModalNews) -> Unit,
                 modifier: Modifier = Modifier,) {

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(count = news.itemCount, key = news.itemKey { item ->
            item.uniqueKey
        }) { index ->

            news[index]?.let {
                NewsCard(article = it, onClick = onNewsClick)
            }
        }

        val loadState = news.loadState
        item {
            when {
                (LoadState.Loading == loadState.refresh) -> {
                    ShimmerEffect()
                }

                (LoadState.Loading == loadState.append) -> {
                    AppViewLoader(Modifier.fillMaxWidth())
                }

                (loadState.hasError) -> {
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
                            onReload = {},
                            modifier = Modifier.fillParentMaxSize()
                        )
                    }
                }

                (news.isEmpty) -> {
                    EmptyScreen("No BookMark Item Found", Modifier.fillParentMaxSize())
                }
            }
        }
    }
}
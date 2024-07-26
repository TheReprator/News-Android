package dev.reprator.news.presentation.newsPane.newsList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import dev.reprator.news.R
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.util.composeUtil.isEmpty
import dev.reprator.news.util.composeUtil.theme.Dimens.MediumPadding1
import dev.reprator.news.util.composeUtil.ui.AppViewError
import dev.reprator.news.util.composeUtil.ui.AppViewLoader
import dev.reprator.news.util.composeUtil.ui.EmptyScreen

@Composable
fun NewsList(
    news: LazyPagingItems<ModalNews>,
    newsClick: (ModalNews) -> Unit,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    selectedNews: ModalNews? = null,
    highlightSelectedNews: Boolean = false
) {
    NewsList(news,newsClick, {}, {}, modifier, lazyListState, selectedNews, highlightSelectedNews)
}

@Composable
fun NewsList(
    news: LazyPagingItems<ModalNews>,
    newsClick: (ModalNews) -> Unit,
    reload: () -> Unit,
    showToast: (String) -> Unit,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    selectedNews: ModalNews? = null,
    highlightSelectedNews: Boolean = false
) {
    LazyColumn(
        state = lazyListState,
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(count = news.itemCount, key = news.itemKey { item ->
            item.id
        }) { index ->

            news[index]?.let {
                val isSelectedItem = highlightSelectedNews && selectedNews?.id == it.id
                NewsItemCard(article = it, onClick = newsClick, isSelected = isSelectedItem)
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
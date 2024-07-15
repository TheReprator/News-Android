package dev.reprator.news.presentation.newsDetail


import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import dev.reprator.news.R
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.presentation.ComposeLocalWrapper
import dev.reprator.news.util.composeUtil.AppIcons
import dev.reprator.news.util.composeUtil.theme.ContrastAwareNewsTheme
import dev.reprator.news.util.composeUtil.ui.AppViewLoader
import dev.reprator.news.util.composeUtil.ui.NewsAppBar

@Composable
internal fun NewsDetailScreen(
    onBackPress: () -> Unit,
    viewModel: NewsDetailViewModel = hiltViewModel()
) {
    var modalNews by remember { mutableStateOf(viewModel.newsItem) }

    NewsDetailScreen(modalNews, onBackPress) {
        modalNews = modalNews.copy(isBookMarked = !modalNews.isBookMarked)
        viewModel.updateBookMarks(modalNews)
    }
}

@Composable
fun NewsDetailScreen(modalNews: ModalNews, onBackPress: () -> Unit, updateBookMark: () -> Unit) {

    var loaderDialogScreen by remember { mutableStateOf(true) }

    Box {
        Column {
            NewsAppBar(modalNews.title, onBackPress)

            WebView(modalNews.url) {
                loaderDialogScreen = it
            }

            if (loaderDialogScreen)
                AppViewLoader(Modifier.fillMaxSize())
        }

        FloatingActionButton(
            onClick = updateBookMark,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 10.dp, bottom = 20.dp),
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        ) {
            val icon = if (modalNews.isBookMarked)
                AppIcons.NAV_SELECTED_BOOKMARK.first to R.string.bookmark_added
            else
                AppIcons.NAV_UNSELECTED_BOOKMARK.first to R.string.bookmark_removed

            Icon(
                imageVector = icon.first,
                contentDescription = stringResource(id = icon.second),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun WebView(url: String, isLoaded: (Boolean) -> Unit) {
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            webViewClient = AppWebViewClient(isLoaded)
        }
    }, update = {
        it.loadUrl(url)
    })
}

@Preview(showBackground = true, name = "Bookmarked News")
@Composable
fun PreviewNewsDetailBookMark() {
    ComposeLocalWrapper {
        ContrastAwareNewsTheme {
            val newsItem = ModalNews(
                "Google", "Vikram", "Titlsdfsdfsdfsdfsdfsfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsde",
                "description", "https://www.google.com", "https://www.google.com",
                System.currentTimeMillis(), "content", true, category = ""
            )
            NewsDetailScreen(newsItem, {}, {})
        }
    }
}

@Preview(showBackground = true, name = "Bookmarked News removed")
@Composable
fun PreviewNewsDetailBookMarkRemoved() {
    ComposeLocalWrapper {
        ContrastAwareNewsTheme {
            val newsItem = ModalNews(
                "Google", "Vikram", "Title",
                "description", "https://www.google.com", "https://www.google.com",
                System.currentTimeMillis(), "content", false, category = ""
            )
            NewsDetailScreen(newsItem, {}, {})
        }
    }
}
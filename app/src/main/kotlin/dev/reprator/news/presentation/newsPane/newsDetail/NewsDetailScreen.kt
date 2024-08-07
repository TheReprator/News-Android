package dev.reprator.news.presentation.newsPane.newsDetail

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import dev.reprator.news.R
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.util.composeUtil.AppIcons
import dev.reprator.news.util.composeUtil.ui.AppViewLoader
import dev.reprator.news.util.composeUtil.ui.NewsAppBar

@Composable
internal fun NewsDetailScreen(
    news: ModalNews,
    shouldShowBack: Boolean,
    onBackPress: () -> Unit,
    updateBookMark: () -> Unit,
    modifier: Modifier = Modifier
) {
    NewsDetailScreenContainer(news, onBackPress, updateBookMark, modifier, shouldShowBack)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NewsDetailScreenContainer(
    news: ModalNews,
    onBackPress: () -> Unit,
    updateBookMark: () -> Unit,
    modifier: Modifier = Modifier,
    shouldShowBack: Boolean = false,
) {
    var loaderDialogScreen by remember { mutableStateOf(true) }

    Scaffold(topBar = {
        NewsAppBar(news.id.title, onBackPress, shouldShowBack = shouldShowBack)
    }, floatingActionButton = {
        NDSFloatingActionButton(news.personalisation.isBookMarked, updateBookMark)
    }) {
        Box(modifier.fillMaxSize()) {
            NDSWebView(news.url, {
                loaderDialogScreen = it
            })

            if (loaderDialogScreen)
                AppViewLoader(Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun NDSWebView(url: String, isLoaded: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    AndroidView(modifier = modifier, factory = {
        android.webkit.WebView(it).apply {
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

@Composable
fun NDSFloatingActionButton(
    isBookMarked: Boolean,
    updateBookMark: () -> Unit,
    modifier: Modifier = Modifier
) {

    FloatingActionButton(
        onClick = updateBookMark,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        modifier = modifier
    ) {
        val icon = if (isBookMarked)
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

package dev.reprator.news.presentation.newsList.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.presentation.newsList.ui.NewsListScreen

const val ROUTE_NEWS = "route_news"

fun NavGraphBuilder.newsScreen(onClick: (ModalNews) -> Unit,
                               showToast:(String) -> Unit,
                               snackBarHostState: SnackbarHostState,) {
    composable(route = ROUTE_NEWS) {
        NewsListScreen(onNewsClick = onClick, showToast, snackBarHostState)
    }
}




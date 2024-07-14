package dev.reprator.news.ui.newsList.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.ui.newsList.NewsListScreen

const val ROUTE_NEWS = "route_news"

fun NavGraphBuilder.newsScreen( onClick: (ModalNews) -> Unit, showToast: (String) -> Unit ) {
    composable(route = ROUTE_NEWS) {
        NewsListScreen(onNewsClick = onClick, showToast)
    }
}




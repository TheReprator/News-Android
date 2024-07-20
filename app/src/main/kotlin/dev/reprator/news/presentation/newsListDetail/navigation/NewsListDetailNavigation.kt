package dev.reprator.news.presentation.newsListDetail.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.reprator.news.presentation.newsListDetail.NewsListDetailScreen

const val ROUTE_NEWS_DETAIL = "route_news_listDetail"

fun NavGraphBuilder.newsListDetailScreen() {
    composable(route = ROUTE_NEWS_DETAIL) {
        NewsListDetailScreen()
    }
}

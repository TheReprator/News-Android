package dev.reprator.news.ui.newsDetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.reprator.news.ui.newsDetail.NewsDetailScreen

const val ROUTE_NEWS_DETAIL = "route_news_detail"

fun NavController.navigateToNewsDetail(navOptions: NavOptions ?= null) = navigate(ROUTE_NEWS_DETAIL, navOptions)

fun NavGraphBuilder.newsDetailScreen(
    onClick: () -> Unit,
    onShowSnackbar: (String) -> Unit,
) {
    composable(route = ROUTE_NEWS_DETAIL) {
        NewsDetailScreen({

        })
    }
}



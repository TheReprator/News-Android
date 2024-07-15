package dev.reprator.news.presentation.newsDetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.presentation.newsDetail.NewsDetailScreen

fun NavController.navigateToNewsDetail(item: ModalNews) =  navigate(item)


fun NavGraphBuilder.newsDetailScreen(onBackClick: () -> Unit) {

    composable<ModalNews> {
        NewsDetailScreen(onBackClick)
    }
}



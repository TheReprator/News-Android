package dev.reprator.news.presentation.bookmarks.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.presentation.bookmarks.BookMarkScreen

const val ROUTE_BOOKMARK = "route_bookmark"


fun NavGraphBuilder.bookmarkScreen(
    onClick: (ModalNews) -> Unit
) {
    composable(route = ROUTE_BOOKMARK) {
        BookMarkScreen({
            onClick(it)
        })
    }
}


package dev.reprator.news.ui.bookmarks.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.reprator.news.ui.newsDetail.BookMarkScreen

const val ROUTE_BOOKMARK = "route_bookmark"


fun NavGraphBuilder.bookmarkScreen(
    onClick: () -> Unit,
    onShowSnackbar: (String) -> Unit,
) {
    composable(route = ROUTE_BOOKMARK) {
        BookMarkScreen({

        })
    }
}


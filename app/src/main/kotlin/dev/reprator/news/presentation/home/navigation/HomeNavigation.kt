package dev.reprator.news.presentation.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.reprator.news.presentation.home.ui.HomeScreen

const val ROUTE_HOME = "home"

fun NavGraphBuilder.homeScreen() {
    composable(route = ROUTE_HOME) {
        HomeScreen()
    }
}

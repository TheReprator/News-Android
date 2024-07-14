package dev.reprator.news.ui.newsList.archModel

sealed interface Action {
    data class Load(val category: String) : Action
}
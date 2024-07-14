package dev.reprator.news.ui.newsList.arch.model

sealed interface Action {
    data class Load(val category: String) : Action
}
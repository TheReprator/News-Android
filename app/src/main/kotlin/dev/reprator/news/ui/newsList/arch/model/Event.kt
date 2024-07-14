package dev.reprator.news.ui.newsList.arch.model

sealed interface Event {
    data class ToastMessage(val message: String) : Event
}
package dev.reprator.news.ui.newsList.archModel

sealed interface Event {
    data class ToastMessage(val message: String) : Event
}
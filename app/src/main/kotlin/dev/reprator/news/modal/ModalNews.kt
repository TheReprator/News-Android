package dev.reprator.news.modal

import androidx.compose.runtime.Immutable

@Immutable
data class ModalNewsList(
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: Long,
    val content: String,
)
package dev.reprator.news.modal

import androidx.compose.runtime.Immutable
import androidx.room.Entity

@Immutable
@Entity(tableName = "news", primaryKeys= [ "title", "source", "author"])
data class ModalNews(
    val source: String,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: Long,
    val content: String,
    val isBookMarked: Boolean = false,
    val category: String = ""
) {
    val uniqueKey by lazy {
        "$publishedAt, $source, $author, $title"
    }
}
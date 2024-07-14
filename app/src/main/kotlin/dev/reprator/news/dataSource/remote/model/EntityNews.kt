package dev.reprator.news.dataSource.remote.model

import android.os.Parcelable
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable


@Serializable
class EntityNewsContainer<T>(
    val status: String?,
    val totalResults: Int?,
    val articles: List<T>?
)

@Serializable
data class EntityNews (
    val source: Source ?,
    val author: String ?,
    val title: String ?,
    val description: String ?,
    val url: String ?,
    val urlToImage: String ?,
    val publishedAt: Instant?,
    val content: String ?,
)

@Serializable
data class Source(
    val id: String ?,
    val name: String ?
)
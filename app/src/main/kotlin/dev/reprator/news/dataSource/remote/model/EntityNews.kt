package dev.reprator.news.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class EntityNews (
    val author: String ?,
    val title: String ?,
    val description: String ?,
    val url: String ?,
    val urlToImage: String ?,
    val publishedAt: Long?,
    val content: String ?,
)
package dev.reprator.news.appDb.model

import androidx.room.Embedded
import androidx.room.Entity

@Entity(tableName = "news", primaryKeys = ["title", "source", "author"])
data class EntityDBNews(
    @Embedded
    val id: EntityDBNewsId,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: Long,
    val content: String,
    @Embedded
    val personalisation: EntityDBNewsPersonalisation,
    val category: String
)

data class EntityDBNewsId(
    val source: String,
    val author: String,
    val title: String
)


data class EntityDBNewsPersonalisation(
    val isBookMarked: Boolean
) {
    constructor() : this(false)
}
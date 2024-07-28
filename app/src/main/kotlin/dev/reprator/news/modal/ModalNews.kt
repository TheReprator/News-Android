package dev.reprator.news.modal

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Entity(tableName = "news", primaryKeys = ["title", "source", "author"])
data class ModalNews(
    @Embedded
    val id: ModalNewsId,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: Long,
    val content: String,
    @Embedded
    val personalisation: ModalNewsPersonalisation,
    val category: String
) : Parcelable


@Parcelize
@Serializable
data class ModalNewsId(
    val source: String,
    val author: String,
    val title: String
) : Parcelable


@Parcelize
@Serializable
data class ModalNewsPersonalisation(
    val isBookMarked: Boolean
): Parcelable {
    constructor(): this(false)
}
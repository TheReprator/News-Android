package dev.reprator.news.modal

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.room.Entity
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Immutable
@Entity(tableName = "news", primaryKeys = ["title", "source", "author"])
data class ModalNews(
    val id: ModalNewsId,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: Long,
    val content: String,
    val personalisation: ModalNewsPersonalisation,
    val category: String
) : Parcelable {
    @IgnoredOnParcel
    val uniqueKey by lazy {
        "$publishedAt, $id"
    }
}


@Parcelize
@Immutable
@Serializable
data class ModalNewsId(
    val source: String,
    val author: String,
    val title: String
) : Parcelable


@Parcelize
@Immutable
@Serializable
data class ModalNewsPersonalisation(
    val isBookMarked: Boolean,
): Parcelable {
    constructor(): this(false)
}
package dev.reprator.news.modal

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.room.Entity
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Immutable
@Serializable
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
    val isBookMarked: Boolean,
    val category: String
): Parcelable {
    @IgnoredOnParcel
    val uniqueKey by lazy {
        "$publishedAt, $source, $author, $title"
    }
}
package dev.reprator.news.dataSource.local

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import dev.reprator.news.modal.ModalNewsId
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Entity(tableName = "remote_key_news", primaryKeys = ["title", "source", "author"])
data class RemoteNewsKeys(
    val id: ModalNewsId,
    val category: String,
    val prevKey: Int?,
    val currentPage: Int,
    val nextKey: Int?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)

@Parcelize
@Immutable
@Serializable
data class DBNewsPersonalisation(
    val isBookMarked: Boolean ?= null,
): Parcelable
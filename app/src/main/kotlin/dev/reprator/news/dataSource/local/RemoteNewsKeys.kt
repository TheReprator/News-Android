package dev.reprator.news.dataSource.local

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import dev.reprator.news.modal.ModalNewsId
import dev.reprator.news.util.pagination.DBRemotePagingEntity
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Entity(tableName = "remote_key_news", primaryKeys = ["title", "source", "author"])
 data class RemoteNewsKeys(
    val id: ModalNewsId,
    val category: String,
    @ColumnInfo(name = "previous_page")
    override val previousPage: Int?,
    @ColumnInfo(name = "current_page")
    override val currentPage: Int,
    @ColumnInfo(name = "next_page")
    override val nextPage: Int?,
    @ColumnInfo(name = "created_at")
    override val createdAt: Long = System.currentTimeMillis()
): DBRemotePagingEntity

@Parcelize
@Immutable
@Serializable
data class DBNewsPersonalisation(
    val isBookMarked: Boolean? = null,
) : Parcelable
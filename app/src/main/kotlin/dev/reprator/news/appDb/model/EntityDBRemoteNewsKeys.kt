package dev.reprator.news.appDb.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import dev.reprator.news.util.pagination.DBRemotePagingEntity

@Entity(tableName = "remote_key_news", primaryKeys = ["title", "source", "author"])
 data class EntityDBRemoteNewsKeys(
   @Embedded
    val id: EntityDBNewsId,
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
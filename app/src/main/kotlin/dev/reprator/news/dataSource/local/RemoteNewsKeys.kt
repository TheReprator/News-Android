package dev.reprator.news.dataSource.local

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "remote_key_news", primaryKeys= [ "title", "source", "author"])
data class RemoteNewsKeys(
    val category: String,
    val source: String,
    val author: String,
    val title: String,
    val prevKey: Int?,
    val currentPage: Int,
    val nextKey: Int?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
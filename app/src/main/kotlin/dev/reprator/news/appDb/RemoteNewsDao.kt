package dev.reprator.news.appDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.reprator.news.dataSource.local.RemoteNewsKeys

@Dao
interface RemoteNewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteNewsKeys>)

    @Query("Select * From remote_key_news Where source IN (:source) and title IN (:title) and author IN (:author)")
    suspend fun getRemoteKeyByNewsID(source: String, title: String, author: String): RemoteNewsKeys?

    @Query("Delete From remote_key_news Where category IN (:category)")
    suspend fun clearRemoteKeysByCategory(category: String)

    @Query("Select created_at From remote_key_news Where category IN (:category) Order By created_at DESC LIMIT 1")
    suspend fun getCreationTimeByCategory(category: String): Long?
}
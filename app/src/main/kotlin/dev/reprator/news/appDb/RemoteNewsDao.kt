package dev.reprator.news.appDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import dev.reprator.news.appDb.model.EntityDBRemoteNewsKeys
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.util.pagination.DBRemotePagingEntity
import dev.reprator.news.util.pagination.PagingRemoteDBDao

@Dao
interface RemoteNewsDao: PagingRemoteDBDao<ModalNews> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<EntityDBRemoteNewsKeys>)

    @Query("Select * From remote_key_news Where source IN (:source) and title IN (:title) and author IN (:author)")
    suspend fun getRemoteKeyByNewsID(source: String, title: String, author: String): EntityDBRemoteNewsKeys?

    @Query("Delete From remote_key_news Where category IN (:category)")
    suspend fun clearRemoteKeysByCategory(category: String)

    @Query("Select created_at From remote_key_news Where category IN (:category) Order By created_at DESC LIMIT 1")
    suspend fun getCreationTimeByCategory(category: String): Long?

    @RawQuery
    suspend fun getItemRawQuery(query: SupportSQLiteQuery): EntityDBRemoteNewsKeys

    @RawQuery
    suspend fun getCreationTimeQuery(query: SupportSQLiteQuery): Long?

    override suspend fun getItem(input: ModalNews): DBRemotePagingEntity {
        val query = "Select * From remote_key_news Where source IN (?) and title IN (?) and author IN (?)"
        val simpleSQLiteQuery = SimpleSQLiteQuery(query, arrayOf(input.id.source, input.id.title, input.id.author))
        return getItemRawQuery(simpleSQLiteQuery)
    }

    override suspend fun getCreationTime(vararg input: String): Long? {
        val query = "Select created_at From remote_key_news Where category IN (?) Order By created_at DESC LIMIT 1"
        val simpleSQLiteQuery = SimpleSQLiteQuery(query, arrayOf<Any>(input[0]))
        return getCreationTimeQuery(simpleSQLiteQuery)
    }

}
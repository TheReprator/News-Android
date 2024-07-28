package dev.reprator.news.appDb

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.reprator.news.appDb.model.EntityDBNews
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<EntityDBNews>)

    @Update
    fun updateNewsByObject(dbNews: EntityDBNews): Int

    @Query("Select * From news where category IN (:category)")
    fun getNews(category: String): DataSource.Factory<Int, EntityDBNews>

    @Query("UPDATE news SET isBookMarked=:isBookMarked Where source IN (:source) and title IN (:title) and author IN (:author)")
    fun updateNews(isBookMarked: Boolean, source: String, title: String, author: String): Int

    @Query("Select * From news where category IN (:category) AND isBookMarked = 1")
    fun getBookMarkedItemsByCategory(category: String): List<EntityDBNews>

    @Query("Select * From news Where source IN (:source) and title IN (:title) and author IN (:author)")
    fun getNewsById(source: String, title: String, author: String): EntityDBNews?

    @Query("Select * From news where isBookMarked = 1")
    fun getBookMarkedItems(): DataSource.Factory<Int, EntityDBNews>

    @Query("Delete From news where category IN (:category)")
    suspend fun clearAllNewsByCategory(category: String)
}
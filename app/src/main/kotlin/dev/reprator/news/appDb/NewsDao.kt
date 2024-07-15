package dev.reprator.news.appDb

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.reprator.news.modal.ModalNews

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<ModalNews>)

    @Query("Select * From news where category IN (:category)")
    fun getNews(category: String): PagingSource<Int, ModalNews>

    @Update
    fun updateNews(dbNews: ModalNews): Int

    @Query("Select * From news where category IN (:category) AND isBookMarked = 1")
    fun getBookMarkedItemsByCategory(category: String): List<ModalNews>

    @Query("Select * From news where isBookMarked = 1")
    fun getBookMarkedItems(): PagingSource<Int, ModalNews>

    @Query("Delete From news where category IN (:category)")
    suspend fun clearAllNewsByCategory(category: String)
}
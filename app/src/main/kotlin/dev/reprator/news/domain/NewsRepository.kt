package dev.reprator.news.domain

import androidx.paging.PagingData
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.modal.ModalNewsId
import dev.reprator.news.util.pagination.PagingHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getNewsStream(newsCategory: String, coroutineScope: CoroutineScope): PagingHandle<ModalNews>

    fun getNewsBookMarkedStream(coroutineScope: CoroutineScope): Flow<PagingData<ModalNews>>

    suspend fun setNewsIsBookMarked(newsId: ModalNewsId, isBookMarked: Boolean)

    fun getNewsById(newsId: ModalNewsId): ModalNews?
}
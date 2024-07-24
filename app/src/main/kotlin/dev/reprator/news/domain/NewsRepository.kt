package dev.reprator.news.domain

import dev.reprator.news.modal.ModalNews
import dev.reprator.news.modal.ModalNewsId
import dev.reprator.news.util.pagination.PagingHandle
import kotlinx.coroutines.CoroutineScope

interface NewsRepository {

    fun getNewsStream(newsCategory: String, coroutineScope: CoroutineScope): PagingHandle<ModalNews>

    suspend fun setNewsIsBookMarked(characterId: ModalNewsId, isBookMarked: Boolean)
}
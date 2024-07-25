package dev.reprator.news.domain

import dev.reprator.news.appDb.model.EntityDBNews
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.modal.ModalNewsId
import dev.reprator.news.util.MapperToFrom
import dev.reprator.news.util.pagination.PagingHandle
import kotlinx.coroutines.CoroutineScope

interface NewsRepository {

    fun getNewsStream(newsCategory: String, coroutineScope: CoroutineScope, mapper:  MapperToFrom<EntityDBNews, ModalNews>): PagingHandle<ModalNews>

    suspend fun setNewsIsBookMarked(newsId: ModalNewsId, isBookMarked: Boolean)
}
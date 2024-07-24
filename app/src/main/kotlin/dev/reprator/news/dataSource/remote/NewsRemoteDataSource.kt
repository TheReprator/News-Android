package dev.reprator.news.dataSource.remote

import dev.reprator.news.modal.ModalNews
import dev.reprator.news.modal.ModalNewsId

interface NewsRemoteDataSource {

    suspend fun getNews(sources: String, page: Int, pageSize: Int): List<ModalNews>

    suspend fun setNewsIsBookMarked(newsId: ModalNewsId, isBookMarked: Boolean)
}
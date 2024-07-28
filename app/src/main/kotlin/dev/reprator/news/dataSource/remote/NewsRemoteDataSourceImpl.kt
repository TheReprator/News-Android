package dev.reprator.news.dataSource.remote

import dev.reprator.news.dataSource.remote.mapper.NewsMapper
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.modal.ModalNewsId
import dev.reprator.news.util.mapAll
import javax.inject.Inject

class NewsRemoteDataSourceImpl @Inject constructor(
    private val apiService: NewsApiService,
    private val mapper: NewsMapper
) : NewsRemoteDataSource {

    override suspend fun getNews(sources: String, page: Int, pageSize: Int): List<ModalNews> {
        val news =
            apiService.getHeadLines(sources = sources, page = page, pageSize = pageSize)
         return mapper.mapAll(sources, news)
    }

    override suspend fun setNewsIsBookMarked(newsId: ModalNewsId, isBookMarked: Boolean) {
        throw Exception("Not for Remote Api")
    }
}
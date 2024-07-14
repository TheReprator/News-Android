package dev.reprator.news.data.repositoryImpl

import androidx.paging.PagingData
import dev.reprator.news.data.datasource.NewsListRemoteDataSource
import dev.reprator.news.domain.repository.NewsListRepository
import dev.reprator.news.modal.ModalNews
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsListDataRepositoryImpl @Inject constructor(private val newsListRemoteDataSource: NewsListRemoteDataSource): NewsListRepository {

    override fun getNews(source: String): Flow<PagingData<ModalNews>> {
        return newsListRemoteDataSource.getNews(source)
    }
}
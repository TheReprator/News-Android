package dev.reprator.news.dataSource.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.reprator.news.appDb.AppNewsDatabase
import dev.reprator.news.data.datasource.NewsListRemoteDataSource
import dev.reprator.news.dataSource.remote.mapper.NewsMapper
import dev.reprator.news.dataSource.remote.paging.NewsPagingSource
import dev.reprator.news.modal.ModalNews
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val PAGE_SIZE = 20

class NewsListRemoteDataSourceImpl @Inject constructor(
    private val newsApi: NewsApiService,
    private val appDb: AppNewsDatabase,
    private val mapper: NewsMapper
) : NewsListRemoteDataSource {

    @OptIn(ExperimentalPagingApi::class)
    override fun getNews(source: String): Flow<PagingData<ModalNews>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = 2,
                initialLoadSize = 19
            ),
            pagingSourceFactory = {
                appDb.getNewsDao().getNews(source)
            },
            remoteMediator = NewsPagingSource(
                newsApi = newsApi, sources = source,
                appDb = appDb, mapper = mapper
            )
        ).flow
    }
}

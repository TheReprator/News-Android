package dev.reprator.news.ui.newsList

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.reprator.news.appDb.AppNewsDatabase
import dev.reprator.news.dataSource.remote.NewsApiService
import dev.reprator.news.dataSource.remote.mapper.NewsMapper
import dev.reprator.news.dataSource.remote.paging.NewsPagingSource
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val appDb: AppNewsDatabase,
    newsApi: NewsApiService,
    mapper: NewsMapper
) : ViewModel() {

    @OptIn(ExperimentalPagingApi::class)
    val news = Pager(
        config = PagingConfig(
            pageSize = 20,
            prefetchDistance = 2,
            initialLoadSize = 19
        ),
        pagingSourceFactory = {
            appDb.getNewsDao().getNews("general")
        },
        remoteMediator = NewsPagingSource(
            newsApi = newsApi, sources = "general",
            appDb = appDb, mapper = mapper
        )
    ).flow

}

/*
 pageSize = 20,
            prefetchDistance = 2,
            initialLoadSize = 19
* */
package dev.reprator.news.presentation.newsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.reprator.news.appDb.AppNewsDatabase
import dev.reprator.news.dataSource.remote.NewsApiService
import dev.reprator.news.dataSource.remote.mapper.NewsMapper
import dev.reprator.news.dataSource.remote.paging.NewsPagingSource
import dev.reprator.news.modal.ModalNews
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val appDb: AppNewsDatabase,
    private val newsApi: NewsApiService,
    private val mapper: NewsMapper,
    private val pagingConfig: PagingConfig
) : ViewModel() {

    private val category = MutableStateFlow("")

    fun setPrimaryCategory(primaryCategory: String) {
        category.update {
            primaryCategory
        }
    }

    @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
    val paginatedNews: Flow<PagingData<ModalNews>> = category
        .filterNot {
            it.isEmpty()
        }
        .distinctUntilChanged()
        .flatMapLatest { primaryCategory ->
            Pager(
                config = pagingConfig,
                pagingSourceFactory = {
                    appDb.getNewsDao().getNews(primaryCategory)
                },
                remoteMediator = NewsPagingSource(
                    newsApi = newsApi, sources = primaryCategory,
                    appDb = appDb, mapper = mapper
                )
            ).flow.cachedIn(viewModelScope)
        }
}
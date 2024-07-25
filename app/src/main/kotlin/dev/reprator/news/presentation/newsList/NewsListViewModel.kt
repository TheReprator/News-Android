package dev.reprator.news.presentation.newsList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.reprator.news.appDb.mapper.DbNewsMapper
import dev.reprator.news.appDb.model.EntityDBNews
import dev.reprator.news.domain.NewsRepository
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.util.MapperToFrom
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject

private const val ARG_NEWS_ITEM = "argNewsItem"

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val mapper: MapperToFrom<EntityDBNews, ModalNews>,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val selectedNews: StateFlow<ModalNews?> by lazy {
        savedStateHandle.getStateFlow(ARG_NEWS_ITEM, savedStateHandle[ARG_NEWS_ITEM])
    }

    fun onNewsClick(news: ModalNews) {
        savedStateHandle[ARG_NEWS_ITEM] = news
    }

    private val category = MutableStateFlow("")

    fun setPrimaryCategory(primaryCategory: String) {
        category.update {
            primaryCategory
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val paginatedNews: Flow<PagingData<ModalNews>> = category
        .filterNot {
            it.isEmpty()
        }
        .distinctUntilChanged()
        .flatMapLatest { primaryCategory ->
            newsRepository.getNewsStream(primaryCategory, viewModelScope, mapper).getPagingDataStream()
        }
}
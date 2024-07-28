package dev.reprator.news.presentation.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.reprator.news.di.impl.AppCoroutineDispatchers
import dev.reprator.news.domain.NewsRepository
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.modal.ModalNewsPersonalisation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ARG_NEWS_ITEM = "argNewsItem"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val dispatchers: AppCoroutineDispatchers,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val selectedNews: StateFlow<ModalNews?> by lazy {
        savedStateHandle.getStateFlow<ModalNews?>(ARG_NEWS_ITEM, savedStateHandle[ARG_NEWS_ITEM])
            .filterNotNull()
            .map {
                newsRepository.getNewsById(it.id)
            }.flowOn(dispatchers.io).stateIn(
                scope = viewModelScope, initialValue = null,
                started = SharingStarted.WhileSubscribed(1_000)
            )
    }

    private val category = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val paginatedNews: Flow<PagingData<ModalNews>> = category
        .filterNot {
            it.isEmpty()
        }
        .distinctUntilChanged()
        .flatMapLatest { primaryCategory ->
            newsRepository.getNewsStream(primaryCategory, viewModelScope).getPagingDataStream()
        }

    fun onNewsClick(news: ModalNews) {
        savedStateHandle[ARG_NEWS_ITEM] = news
    }

    fun setPrimaryCategory(primaryCategory: String) {
        category.update {
            primaryCategory
        }
    }

    fun updateBookMarks() {
        viewModelScope.launch(dispatchers.io) {
            val item = selectedNews.value!!
            val update =
                item.copy(personalisation = ModalNewsPersonalisation(!item.personalisation.isBookMarked))
            newsRepository.setNewsIsBookMarked(
                item.id,
                update.personalisation.isBookMarked
            )
            onNewsClick(update)
        }
    }
}
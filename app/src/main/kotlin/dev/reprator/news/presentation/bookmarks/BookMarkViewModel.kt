package dev.reprator.news.presentation.bookmarks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.reprator.news.di.impl.AppCoroutineDispatchers
import dev.reprator.news.domain.NewsRepository
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.modal.ModalNewsPersonalisation
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ARG_BOOKMARK_ITEM = "argNewsBookMarkItem"

@HiltViewModel
class BookViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val dispatchers: AppCoroutineDispatchers,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val selectedNews: StateFlow<ModalNews?> by lazy {
        savedStateHandle.getStateFlow<ModalNews?>(ARG_BOOKMARK_ITEM, savedStateHandle[ARG_BOOKMARK_ITEM])
            .filterNotNull()
            .map {
                newsRepository.getNewsById(it.id)
            }.flowOn(dispatchers.io).stateIn(
                scope = viewModelScope, initialValue = null,
                started = SharingStarted.WhileSubscribed(1_000)
            )
    }

    val news = newsRepository.getNewsBookMarkedStream(viewModelScope)

    fun onNewsClick(news: ModalNews) {
        savedStateHandle[ARG_BOOKMARK_ITEM] = news
    }

    fun updateBookMarks() {
        viewModelScope.launch(dispatchers.io) {
            val update = selectedNews.value!!.copy(personalisation = ModalNewsPersonalisation(!selectedNews.value!!.personalisation.isBookMarked))
            newsRepository.setNewsIsBookMarked(selectedNews.value!!.id, update.personalisation.isBookMarked)
            onNewsClick(update)
        }
    }
}
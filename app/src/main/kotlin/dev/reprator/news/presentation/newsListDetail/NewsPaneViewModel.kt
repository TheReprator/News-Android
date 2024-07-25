package dev.reprator.news.presentation.newsListDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.reprator.news.appDb.NewsDao
import dev.reprator.news.di.impl.AppCoroutineDispatchers
import dev.reprator.news.domain.NewsRepository
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.modal.ModalNewsPersonalisation
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ARG_NEWS_ITEM = "argNewsItem"

@HiltViewModel
class NewsPaneViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val newsRepository: NewsRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : ViewModel() {

    val selectedNews: StateFlow<ModalNews?> by lazy {
        savedStateHandle.getStateFlow(ARG_NEWS_ITEM, savedStateHandle[ARG_NEWS_ITEM])
    }

    fun onNewsClick(news: ModalNews) {
        savedStateHandle[ARG_NEWS_ITEM] = news
    }

    fun updateBookMarks() {
        viewModelScope.launch(dispatchers.io) {
            val update = selectedNews.value!!.copy(personalisation = ModalNewsPersonalisation(!selectedNews.value!!.personalisation.isBookMarked))
            onNewsClick(update)
            newsRepository.setNewsIsBookMarked(selectedNews.value!!.id, update.personalisation.isBookMarked)
        }
    }
}

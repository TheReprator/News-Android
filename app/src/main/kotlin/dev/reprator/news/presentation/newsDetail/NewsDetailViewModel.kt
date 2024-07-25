package dev.reprator.news.presentation.newsDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.reprator.news.appDb.NewsDao
import dev.reprator.news.di.impl.AppCoroutineDispatchers
import dev.reprator.news.modal.ModalNews
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val localNewsDao: NewsDao,
    private val dispatchers: AppCoroutineDispatchers,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val newsItem by lazy {
        savedStateHandle.toRoute<ModalNews>()
    }

    fun updateBookMarks(item: ModalNews) {
        viewModelScope.launch(dispatchers.io) {
           // localNewsDao.updateNews(item)
        }
    }
}
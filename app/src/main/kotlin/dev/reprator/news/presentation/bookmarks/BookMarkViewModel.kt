package dev.reprator.news.presentation.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.reprator.news.appDb.NewsDao
import dev.reprator.news.modal.ModalNews
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(private val newsDao: NewsDao,
                                        pageConfig: PagingConfig) : ViewModel() {

    val news: Flow<PagingData<ModalNews>> = Pager(
        config = pageConfig,
        pagingSourceFactory = {
            newsDao.getBookMarkedItems()
        }).flow.cachedIn(viewModelScope)
}
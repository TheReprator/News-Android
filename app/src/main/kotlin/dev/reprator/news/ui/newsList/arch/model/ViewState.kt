package dev.reprator.news.ui.newsList.arch.model

import androidx.paging.PagingData
import dev.reprator.news.modal.ModalNews
import kotlinx.coroutines.flow.Flow

data class ViewState(
    val newsStates: Flow<PagingData<ModalNews>>,
)

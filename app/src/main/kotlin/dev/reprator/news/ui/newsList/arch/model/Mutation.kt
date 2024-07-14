package dev.reprator.news.ui.newsList.archModel

import androidx.paging.PagingData
import dev.reprator.news.modal.ModalNews
import kotlinx.coroutines.flow.Flow

sealed interface Mutation {
    data class ShowContent(val newsPagedData: Flow<PagingData<ModalNews>>) : Mutation
}
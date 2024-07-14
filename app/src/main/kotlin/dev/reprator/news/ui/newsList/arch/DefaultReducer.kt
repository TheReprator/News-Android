package dev.reprator.news.ui.newsList.arch

import androidx.paging.PagingData
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.ui.newsList.arch.model.Mutation
import dev.reprator.news.ui.newsList.arch.model.ViewState
import dev.reprator.news.util.arch.Reducer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultReducer @Inject constructor() : Reducer<Mutation, ViewState> {

    override fun invoke(mutation: Mutation, currentState: ViewState): ViewState =
        when (mutation) {
            is Mutation.ShowContent ->
                currentState.mutateToShowContent(news = mutation.newsPagedData)
        }

    private fun ViewState.mutateToShowContent(news: Flow<PagingData<ModalNews>>) =
        copy(newsStates = news)
}
package dev.reprator.news.ui.newsList

import androidx.paging.PagingData
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.ui.newsList.archModel.Mutation
import dev.reprator.news.ui.newsList.archModel.ViewState
import dev.reprator.news.util.arch.Reducer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//errorMessage = resources.getString(R.string.newslist_text_generic_error).format(exception.message),

class DefaultReducer @Inject constructor() : Reducer<Mutation, ViewState> {

    override fun invoke(mutation: Mutation, currentState: ViewState): ViewState =
        when (mutation) {
            is Mutation.ShowContent ->
                currentState.mutateToShowContent(users = mutation.newsPagedData)
        }

    private fun ViewState.mutateToShowContent(users: Flow<PagingData<ModalNews>>) =
        copy(newsStates = users)
}
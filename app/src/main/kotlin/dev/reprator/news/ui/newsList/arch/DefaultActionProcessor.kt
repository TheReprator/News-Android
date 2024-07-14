package dev.reprator.news.ui.newsList.arch

import dev.reprator.news.domain.usecase.NewsListUseCase
import dev.reprator.news.ui.newsList.arch.model.Action
import dev.reprator.news.ui.newsList.arch.model.Event
import dev.reprator.news.ui.newsList.arch.model.Mutation
import dev.reprator.news.util.arch.ActionProcessor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultActionProcessor @Inject constructor(
    private val newsUseCase: NewsListUseCase
) : ActionProcessor<Action, Mutation, Event> {

    override fun invoke(action: Action): Flow<Pair<Mutation?, Event?>> =
        flow {
            when (action) {
                is Action.Load -> load(action.category)
                else -> {
                    //no-op
                }
            }
        }

    private suspend fun FlowCollector<Pair<Mutation?, Event?>>.load(category: String) {
            val result = newsUseCase.invoke(category)
            emit(Mutation.ShowContent(newsPagedData = result) to null)
    }

}
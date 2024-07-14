package dev.reprator.news.util.arch

interface Reducer<Mutation, ViewState> {
    operator fun invoke(mutation: Mutation, currentState: ViewState): ViewState
}

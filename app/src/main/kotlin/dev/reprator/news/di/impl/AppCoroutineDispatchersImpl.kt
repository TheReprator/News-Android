package dev.reprator.news.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

interface AppCoroutineDispatchers {
    val main: CoroutineDispatcher
    val computation: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val singleThread: CoroutineDispatcher
}

data class AppCoroutineDispatchersImpl @OptIn(ExperimentalCoroutinesApi::class) constructor(
    override val main: CoroutineDispatcher = Dispatchers.Main,
    override val default: CoroutineDispatcher = Dispatchers.IO.limitedParallelism(1),
    override val io: CoroutineDispatcher = Dispatchers.IO.limitedParallelism(1),
    override val computation: CoroutineDispatcher = Dispatchers.Default,
    override val singleThread: CoroutineDispatcher = Dispatchers.IO
) : AppCoroutineDispatchers
package dev.reprator.news.di.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

interface AppCoroutineDispatchers {
    val main: CoroutineDispatcher
    val computation: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val singleThread: CoroutineDispatcher
}

@OptIn(ExperimentalCoroutinesApi::class)
data class AppCoroutineDispatchersImpl @Inject constructor(
    override val main: CoroutineDispatcher,
    override val default: CoroutineDispatcher,
    override val io: CoroutineDispatcher,
    override val computation: CoroutineDispatcher,
    override val singleThread: CoroutineDispatcher
) : AppCoroutineDispatchers {
    constructor(): this(Dispatchers.Main, Dispatchers.IO.limitedParallelism(1),
        Dispatchers.IO.limitedParallelism(4), Dispatchers.Default, Dispatchers.IO)
}
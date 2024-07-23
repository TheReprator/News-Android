package dev.reprator.news.util.pagination

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

class PagingHandleImpl<T : Any>(
    cachedPager: BaseCachedPager<T, *, *>,
) : PagingHandle<T> {

    override val getPagingDataStream: () -> Flow<PagingData<T>> = { cachedPager.pagingDataStream }

    override val refresh: () -> Unit = { cachedPager.refresh() }
    override val retry: () -> Unit = { cachedPager.retry() }
}

interface PagingHandle<T : Any> {

    val getPagingDataStream: () -> Flow<PagingData<T>>

    val refresh: () -> Unit

    val retry: () -> Unit
}

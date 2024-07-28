package dev.reprator.news.util.pagination

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

class PagingHandleImpl<T : Any>(
    cachedPager: BaseCachedPager<T, *, *>,
) : PagingHandle<T> {

    override val getPagingDataStream: () -> Flow<PagingData<T>> = { cachedPager.pagingDataStream }
}

interface PagingHandle<T : Any> {

    val getPagingDataStream: () -> Flow<PagingData<T>>
}

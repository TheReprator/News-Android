package dev.reprator.news.util.pagination

import androidx.paging.DataSource
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@OptIn(ExperimentalPagingApi::class)
abstract class BaseCachedPager<DataType : Any, IdKey : Any, Personalisation : Any>(
    coroutineScope: CoroutineScope,
    private val pageConfig: PagingConfig,
    private val daoFetcher: () -> DataSource.Factory<Int, DataType>,
) {
    private var pagingSource: DefaultPagingSource<DataType>? = null
    val pagingDataStream: Flow<PagingData<DataType>> by lazy {

        Pager(
            config = pageConfig,
            pagingSourceFactory = daoFetcher().asPagingSourceFactory(),
            remoteMediator = createPagingSource().apply { pagingSource = this }
        ).flow
            .cachedIn(coroutineScope)
            .combine(getCachedInfoStream()) { pagingData, cachedPersonalisation ->
                pagingData.map { item ->
                    mergeWithCache(item, cachedPersonalisation)
                }
            }
    }

    abstract fun getCachedInfoStream(): Flow<Map<IdKey, Personalisation>>

    abstract fun mergeWithCache(item: DataType, cachedInfo: Map<IdKey, Personalisation>): DataType

    abstract fun createPagingSource(): DefaultPagingSource <DataType>

    open fun createPagingHandle(): PagingHandle<DataType> {
        return PagingHandleImpl(this)
    }
}

package dev.reprator.news.util.pagination

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.RawQuery
import timber.log.Timber
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class DefaultPagingSource<T : Any>(
    private val remoteDao: RemoteDBDao,
    val dataFetcher: suspend (Boolean, Int, PagingState<Int, T>) -> List<T>,
) : RemoteMediator<Int, T>() {

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, T>): DBRemotePagingEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.let { localEntity ->
                remoteDao.getItem(localEntity)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, T>): DBRemotePagingEntity? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { localEntity ->
            remoteDao.getItem(localEntity)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, T>): DBRemotePagingEntity? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { localEntity ->
            remoteDao.getItem(localEntity)
        }
    }


    @ExperimentalPagingApi
    override suspend fun load(loadType: LoadType, state: PagingState<Int, T>): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextPage?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.previousPage
                prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextPage
                nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        Timber.e("MovieRemoteMediator: load() called with: loadType = $loadType, " +
                "itemsCount = ${state.pages.count()}, page: $page, " +
                "anchorPosition=${state.anchorPosition}, stateLastItem = ${state.isEmpty()}")

        return try {
            val response = dataFetcher(loadType == LoadType.REFRESH, page, state)
            MediatorResult.Success(endOfPaginationReached = response.isEmpty())
        } catch (error: Exception) {
            MediatorResult.Error(error)
        }
    }

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(6, TimeUnit.DAYS)
        val lastUpdateBySource = remoteDao.getCreationTime()?.createdAt

        return if (System.currentTimeMillis() - (lastUpdateBySource ?: 0) < cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }
}


interface RemoteDBDao {
    @RawQuery
    suspend fun <T>getItem(input: T): DBRemotePagingEntity
    @RawQuery
    suspend fun getCreationTime(vararg input: String): DBRemotePagingEntity?
}

interface DBRemotePagingEntity {
    val previousPage: Int?
    val nextPage: Int?
    val currentPage: Int
    val createdAt: Long
}
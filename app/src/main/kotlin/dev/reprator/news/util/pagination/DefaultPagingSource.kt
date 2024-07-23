package dev.reprator.news.util.pagination

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class DefaultPagingSource<T : Any>(
    val initializeAction: suspend () -> InitializeAction,
    private val remoteDB: RemoteDB<T>,
    val dataFetcher: suspend (Int, PagingState<Int, T>) -> MediatorResult,
) : RemoteMediator<Int, T>() {

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, T>): Int {
        val output = state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.let { localEntity ->
                remoteDB.getNextIndex(localEntity)
            }
        }
        return output?.minus(1) ?: 1
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, T>): Int? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { localEntity ->
            remoteDB.getPreviousIndex(localEntity)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, T>): Int? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { localEntity ->
            remoteDB.getPreviousIndex(localEntity)
        }
    }


    @ExperimentalPagingApi
    override suspend fun load(loadType: LoadType, state: PagingState<Int, T>): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                getRemoteKeyClosestToCurrentPosition(state)
            }

            LoadType.PREPEND -> {
                getRemoteKeyForFirstItem(state) ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )
            }

            LoadType.APPEND -> {
                getRemoteKeyForLastItem(state) ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )
            }
        }

        Timber.e("MovieRemoteMediator: load() called with: loadType = $loadType, " +
                "itemsCount = ${state.pages.count()}, page: $page, " +
                "anchorPosition=${state.anchorPosition}, stateLastItem = ${state.isEmpty()}")

        return try {
            dataFetcher(page, state)
        } catch (error: Exception) {
            MediatorResult.Error(error)
        }
    }

    override suspend fun initialize(): InitializeAction {
        return initializeAction()
    }
}


interface RemoteDB<T> {
    fun getNextIndex(input: T): Int
    fun getPreviousIndex(input: T): Int
}
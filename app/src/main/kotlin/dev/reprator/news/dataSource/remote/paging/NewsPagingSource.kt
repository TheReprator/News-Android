package dev.reprator.news.dataSource.remote.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.reprator.news.appDb.AppNewsDatabase
import dev.reprator.news.dataSource.local.RemoteNewsKeys
import dev.reprator.news.dataSource.remote.NewsApiService
import dev.reprator.news.dataSource.remote.mapper.NewsMapper
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.util.mapAll
import timber.log.Timber
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class NewsPagingSource(
    private val newsApi: NewsApiService,
    private val sources: String,
    private val appDb: AppNewsDatabase,
    private val mapper: NewsMapper
) : RemoteMediator<Int, ModalNews>() {

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ModalNews>): RemoteNewsKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.let { localEntity ->
                appDb.getRemoteKeysDao()
                    .getRemoteKeyByNewsID(localEntity.source, localEntity.title, localEntity.author)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ModalNews>): RemoteNewsKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { localEntity ->
            appDb.getRemoteKeysDao()
                .getRemoteKeyByNewsID(localEntity.source, localEntity.title, localEntity.author)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ModalNews>): RemoteNewsKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { localEntity ->
            appDb.getRemoteKeysDao()
                .getRemoteKeyByNewsID(localEntity.source, localEntity.title, localEntity.author)
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ModalNews>
    ): MediatorResult {

        val page: Int = when(loadType){
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        Timber.e("MovieRemoteMediator: load() called with: loadType = $loadType, itemsCount = ${state.pages.count()}, page: $page, anchorPosition=${state.anchorPosition}, stateLastItem = ${state.isEmpty()}, sources=$sources")

        return try {
            val news =
                newsApi.getHeadLines(sources = sources, page = page, pageSize = state.config.pageSize)

            val endOfPaginationReached = news.isEmpty()

            appDb.withTransaction {

                val bookMarkList = mutableListOf<ModalNews>()

                if (loadType == LoadType.REFRESH) {
                    bookMarkList.addAll(appDb.getNewsDao().getBookMarkedItemsByCategory(sources))

                    appDb.getRemoteKeysDao().clearRemoteKeysByCategory(sources)
                    appDb.getNewsDao().clearAllNewsByCategory(sources)
                }

                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (endOfPaginationReached) null else page + 1

                val itemModalNewsList = mapper.mapAll(news)

                val remoteList = itemModalNewsList.map {
                    RemoteNewsKeys(
                        category = sources,
                        source = it.source,
                        title = it.title,
                        author = it.author,
                        prevKey = prevKey,
                        currentPage = page,
                        nextKey = nextKey
                    )
                }

                val localList = itemModalNewsList.map {

                    val isBookMarkedIndex = bookMarkList.indexOfFirst { item ->
                        (item.author.equals(it.author, true) && item.title.equals(it.title, true))
                    }
                    val isBookMarked = -1 < isBookMarkedIndex

                    it.copy(isBookMarked = isBookMarked, category = sources)
                }

                appDb.getRemoteKeysDao().insertAll(remoteList)
                appDb.getNewsDao().insertAll(localList)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (error: Exception) {
            MediatorResult.Error(error)
        }
    }

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(6, TimeUnit.DAYS)
        val lastUpdateBySource = appDb.getRemoteKeysDao().getCreationTimeByCategory(category = sources)

        return if (System.currentTimeMillis() - (lastUpdateBySource ?: 0) < cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }

    }
}
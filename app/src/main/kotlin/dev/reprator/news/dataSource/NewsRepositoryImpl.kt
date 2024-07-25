package dev.reprator.news.dataSource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingConfig
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.reprator.news.appDb.AppNewsDatabase
import dev.reprator.news.appDb.model.EntityDBNews
import dev.reprator.news.appDb.model.EntityDBNewsId
import dev.reprator.news.dataSource.cache.CachedNewsPager
import dev.reprator.news.dataSource.cache.NewsCache
import dev.reprator.news.appDb.model.EntityDBRemoteNewsKeys
import dev.reprator.news.dataSource.remote.NewsRemoteDataSource
import dev.reprator.news.domain.NewsRepository
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.modal.ModalNewsId
import dev.reprator.news.modal.ModalNewsPersonalisation
import dev.reprator.news.util.MapperToFrom
import dev.reprator.news.util.pagination.DefaultPagingSource
import dev.reprator.news.util.pagination.PagingHandle
import kotlinx.coroutines.CoroutineScope
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: NewsRemoteDataSource,
    private val newsCache: NewsCache,
    private val appDb: AppNewsDatabase,
    private val pageConfig: PagingConfig
) : NewsRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getNewsStream(newsCategory: String, coroutineScope: CoroutineScope, mapper: MapperToFrom<EntityDBNews, ModalNews>): PagingHandle<ModalNews> {

        suspend fun findMediatorAction(): RemoteMediator.InitializeAction {
            val cacheTimeout = TimeUnit.MILLISECONDS.convert(6, TimeUnit.DAYS)
            val lastUpdateBySource = appDb.getRemoteKeysDao().getCreationTime(newsCategory)

            return if (System.currentTimeMillis() - (lastUpdateBySource ?: 0) < cacheTimeout) {
                RemoteMediator.InitializeAction.SKIP_INITIAL_REFRESH
            } else {
                RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH
            }
        }

        val defaultPagingSource = DefaultPagingSource(appDb.getRemoteKeysDao(), {
            findMediatorAction()
        }) {  isRefresh, page, pagingState ->
            val apiList =  remoteDataSource.getNews(newsCategory, page, pagingState.config.pageSize)
            handleResponse(apiList, newsCategory, isRefresh, page, mapper)
        }

        val cachedNewsPager = CachedNewsPager(pageConfig, newsCache, coroutineScope, {
            appDb.getNewsDao().getNews(newsCategory).map {
                mapper.mapTo(it)
            }
        }) {
            defaultPagingSource
        }.createPagingHandle()

        return cachedNewsPager
    }

    private suspend fun handleResponse(newsList: List<ModalNews>, newsCategory: String,
                                       isRefresh: Boolean, page: Int,
                                       mapper:  MapperToFrom<EntityDBNews, ModalNews>): List<ModalNews> {
        return appDb.withTransaction {
            val bookMarkList = mutableListOf<EntityDBNews>()

            if (isRefresh) {
                val mappedResult = appDb.getNewsDao().getBookMarkedItemsByCategory(newsCategory)
                bookMarkList.addAll(mappedResult)

                appDb.getRemoteKeysDao().clearRemoteKeysByCategory(newsCategory)
                appDb.getNewsDao().clearAllNewsByCategory(newsCategory)
            }

            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (newsList.isEmpty()) null else page + 1

            val remoteList = newsList.map {
                EntityDBRemoteNewsKeys(
                    id = EntityDBNewsId(it.id.source, it.id.author, it.id.title),
                    category = newsCategory,
                    previousPage = prevKey,
                    currentPage = page,
                    nextPage = nextKey
                )
            }

            val localList = newsList.map {

                val isBookMarkedIndex = bookMarkList.indexOfFirst { item ->
                    item.id.author == it.id.author &&  item.id.title == it.id.title &&  item.id.source == it.id.source
                }
                val isBookMarked = -1 < isBookMarkedIndex
                it.copy(personalisation = ModalNewsPersonalisation(isBookMarked))
            }

            appDb.getRemoteKeysDao().insertAll(remoteList)
            appDb.getNewsDao().insertAll(localList.map { mapper.mapFrom(it) })
            localList
        }
    }

    override suspend fun setNewsIsBookMarked(newsId: ModalNewsId, isBookMarked: Boolean) {
        newsCache.updateNewsIsBookMarked(newsId, isBookMarked)
        appDb.getNewsDao().updateNews(isBookMarked, newsId.source, newsId.title, newsId.author)
    }
}
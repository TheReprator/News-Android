package dev.reprator.news.dataSource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.room.withTransaction
import dev.reprator.news.appDb.AppNewsDatabase
import dev.reprator.news.appDb.model.EntityDBNews
import dev.reprator.news.appDb.model.EntityDBNewsId
import dev.reprator.news.appDb.model.EntityDBRemoteNewsKeys
import dev.reprator.news.dataSource.cache.CachedNewsPager
import dev.reprator.news.dataSource.cache.NewsCache
import dev.reprator.news.dataSource.remote.NewsRemoteDataSource
import dev.reprator.news.domain.NewsRepository
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.modal.ModalNewsId
import dev.reprator.news.modal.ModalNewsPersonalisation
import dev.reprator.news.util.MapperToFrom
import dev.reprator.news.util.pagination.DefaultPagingSource
import dev.reprator.news.util.pagination.PagingHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: NewsRemoteDataSource,
    private val newsCache: NewsCache,
    private val appDb: AppNewsDatabase,
    private val pageConfig: PagingConfig,
    private val mapper: MapperToFrom<EntityDBNews, ModalNews>
) : NewsRepository {

    override fun getNewsStream(
        newsCategory: String,
        coroutineScope: CoroutineScope
    ): PagingHandle<ModalNews> {

        val defaultPagingSource = DefaultPagingSource(appDb.getRemoteKeysDao(), {
            newsCategory
        }, { input ->
            EntityDBNewsId(input.id.source, input.id.author, input.id.title)
        }) { isRefresh, page, pagingState ->
            val apiList = remoteDataSource.getNews(newsCategory, page, pagingState.config.pageSize)
            handleResponse(apiList, newsCategory, isRefresh, page, mapper)
        }

        val cachedNewsPager = CachedNewsPager(coroutineScope, pageConfig, newsCache, {
            appDb.getNewsDao().getNews(newsCategory).map {
                mapper.mapTo(it)
            }
        }) {
            defaultPagingSource
        }.createPagingHandle()

        return cachedNewsPager
    }

    override fun getNewsBookMarkedStream(
        coroutineScope: CoroutineScope
    ): Flow<PagingData<ModalNews>> = Pager(
        config = pageConfig,
        pagingSourceFactory = appDb.getNewsDao().getBookMarkedItems().map {
            mapper.mapTo(it)
        }.asPagingSourceFactory()
    ).flow.cachedIn(coroutineScope)

    private suspend fun handleResponse(
        newsList: List<ModalNews>, newsCategory: String,
        isRefresh: Boolean, page: Int,
        mapper: MapperToFrom<EntityDBNews, ModalNews>
    ): List<ModalNews> {
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
                    item.id.author == it.id.author && item.id.title == it.id.title && item.id.source == it.id.source
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
        newsCache.updateNewsIsBookMarked(
            EntityDBNewsId(newsId.source, newsId.author, newsId.title),
            isBookMarked
        )
        appDb.getNewsDao().updateNews(isBookMarked, newsId.source, newsId.title, newsId.author)
    }

    override fun getNewsById(newsId: ModalNewsId): ModalNews? {
        val it = appDb.getNewsDao().getNewsById(
            newsId.source,
            newsId.title,
            newsId.author
        )
        return if (null == it)
            null
        else
            mapper.mapTo(it)
    }
}
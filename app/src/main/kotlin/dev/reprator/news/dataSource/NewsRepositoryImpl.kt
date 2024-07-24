package dev.reprator.news.dataSource

import androidx.room.withTransaction
import dev.reprator.news.appDb.AppNewsDatabase
import dev.reprator.news.dataSource.cache.CachedNewsPager
import dev.reprator.news.dataSource.cache.NewsCache
import dev.reprator.news.dataSource.local.RemoteNewsKeys
import dev.reprator.news.dataSource.remote.NewsRemoteDataSource
import dev.reprator.news.domain.NewsRepository
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.modal.ModalNewsId
import dev.reprator.news.util.mapAll
import dev.reprator.news.util.pagination.DefaultPagingSource
import dev.reprator.news.util.pagination.PagingHandle
import kotlinx.coroutines.CoroutineScope

class NewsRepositoryImpl(
    private val remoteDataSource: NewsRemoteDataSource,
    private val newsCache: NewsCache,
    private val appDb: AppNewsDatabase
) : NewsRepository {

    override fun getNewsStream(newsCategory: String, coroutineScope: CoroutineScope): PagingHandle<ModalNews> {
        return CachedNewsPager(newsCache, coroutineScope, {
            appDb.getNewsDao().getNews(newsCategory)
        }) {
            DefaultPagingSource(appDb.getRemoteKeysDao()) { isRefresh, page, pagingState ->
               val apiList =  remoteDataSource.getNews("", page, pagingState.config.pageSize)
                apiList
            }
        }.createPagingHandle()
    }

    private suspend fun tp(newsList: List<ModalNews>, newsCategory: String, isRefresh: Boolean, page: Int) {
        appDb.withTransaction {
            val bookMarkList = mutableListOf<ModalNews>()

            if (isRefresh) {
                bookMarkList.addAll(appDb.getNewsDao().getBookMarkedItemsByCategory(newsCategory))

                appDb.getRemoteKeysDao().clearRemoteKeysByCategory(newsCategory)
                appDb.getNewsDao().clearAllNewsByCategory(newsCategory)
            }

            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (newsList.isEmpty()) null else page + 1

            val remoteList = newsList.map {
                RemoteNewsKeys(
                    category = newsCategory,
                    source = it.source,
                    title = it.title,
                    author = it.author,
                    prevKey = prevKey,
                    currentPage = page,
                    nextKey = nextKey
                )
            }

            val localList = newsList.map {

                val isBookMarkedIndex = bookMarkList.indexOfFirst { item ->
                    (item.author.equals(it.author, true) && item.title.equals(it.title, true))
                }
                val isBookMarked = -1 < isBookMarkedIndex

                it.copy(isBookMarked = isBookMarked, category = sources)
            }

            appDb.getRemoteKeysDao().insertAll(remoteList)
            appDb.getNewsDao().insertAll(localList)
        }
    }

    override suspend fun setNewsIsBookMarked(characterId: ModalNewsId, isBookMarked: Boolean) {
        /*characterCache.updateCharacterIsLiked(characterId, isLiked)
        try {
            remoteDataSource.setCharacterIsLiked(characterId, isLiked)
        } catch (e: Exception) {
            characterCache.updateCharacterIsLiked(characterId, !isLiked)
            throw e
        }*/

        TODO()
    }
}
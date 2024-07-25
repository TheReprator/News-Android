package dev.reprator.news.dataSource.cache

import androidx.paging.DataSource
import androidx.paging.PagingConfig
import dev.reprator.news.appDb.model.EntityDBNewsId
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.modal.ModalNewsPersonalisation
import dev.reprator.news.util.pagination.BaseCachedPager
import dev.reprator.news.util.pagination.DefaultPagingSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class CachedNewsPager(
    coroutineScope: CoroutineScope,
    pageConfig: PagingConfig,
    private val characterCache: NewsCache,
    daoFetcher: () -> DataSource.Factory<Int, ModalNews>,
    private val pagingFactory: () -> DefaultPagingSource<ModalNews, EntityDBNewsId>,
) : BaseCachedPager<ModalNews, EntityDBNewsId, ModalNewsPersonalisation>(
    coroutineScope, pageConfig, daoFetcher
) {

    override fun getCachedInfoStream(): Flow<Map<EntityDBNewsId, ModalNewsPersonalisation>> {
        return characterCache.getNewsPersonalisationStream()
    }

    override fun mergeWithCache(
        item: ModalNews,
        cachedInfo: Map<EntityDBNewsId, ModalNewsPersonalisation>
    ): ModalNews {
        val entityDBNewsId = EntityDBNewsId(item.id.source, item.id.author, item.id.title)
        val cachedPersonalisation = cachedInfo[entityDBNewsId]

        val result =  if (null != cachedPersonalisation) {
            val personalisation = ModalNewsPersonalisation(
                isBookMarked = cachedPersonalisation.isBookMarked)
            item.copy(personalisation = personalisation)
        } else {
            item
        }

        return result
    }

    override fun createPagingSource(): DefaultPagingSource<ModalNews, EntityDBNewsId> {
        return pagingFactory()
    }
}
package dev.reprator.news.dataSource.cache

import androidx.paging.DataSource
import androidx.paging.PagingConfig
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.modal.ModalNewsId
import dev.reprator.news.modal.ModalNewsPersonalisation
import dev.reprator.news.util.pagination.BaseCachedPager
import dev.reprator.news.util.pagination.DefaultPagingSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class CachedNewsPager(
    pageConfig: PagingConfig,
    private val characterCache: NewsCache,
    coroutineScope: CoroutineScope,
    daoFetcher: () -> DataSource.Factory<Int, ModalNews>,
    private val pagingFactory: () -> DefaultPagingSource<ModalNews>,
) : BaseCachedPager<ModalNews, ModalNewsId, ModalNewsPersonalisation>(
    coroutineScope, pageConfig, daoFetcher
) {

    override fun getCachedInfoStream(): Flow<Map<ModalNewsId, ModalNewsPersonalisation>> {
        return characterCache.getNewsPersonalisationStream()
    }

    override fun mergeWithCache(
        item: ModalNews,
        cachedInfo: Map<ModalNewsId, ModalNewsPersonalisation>
    ): ModalNews {
        val cachedPersonalisation = cachedInfo[item.id]
        val result =  if (null != cachedPersonalisation) {
            val personalisation = ModalNewsPersonalisation(
                isBookMarked = cachedPersonalisation.isBookMarked)
            item.copy(personalisation = personalisation)
        } else {
            item
        }

        return result
    }

    override fun createPagingSource(): DefaultPagingSource<ModalNews> {
        return pagingFactory()
    }
}
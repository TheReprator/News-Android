package dev.reprator.news.dataSource.cache

import androidx.paging.PagingSource
import dev.reprator.news.dataSource.local.DBNewsPersonalisation
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.modal.ModalNewsId
import dev.reprator.news.util.pagination.BaseCachedPager
import dev.reprator.news.util.pagination.DefaultPagingSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class CachedNewsPager(
    private val characterCache: NewsCache,
    coroutineScope: CoroutineScope,
    daoFetcher: () -> PagingSource<Int, ModalNews>,
    private val pagingFactory: () -> DefaultPagingSource<ModalNews>,
) : BaseCachedPager<ModalNews, ModalNewsId, DBNewsPersonalisation>(coroutineScope, daoFetcher) {

    override fun getCachedInfoStream(): Flow<Map<ModalNewsId, DBNewsPersonalisation>> {
        return characterCache.getNewsPersonalisationStream()
    }

    override fun mergeWithCache(item: ModalNews, cachedInfo: Map<ModalNewsId, DBNewsPersonalisation>): ModalNews {
        return item.resolveCachedPersonalisation(cachedInfo)
    }

    override fun createPagingSource(): DefaultPagingSource<ModalNews> {
        return pagingFactory()
    }
}

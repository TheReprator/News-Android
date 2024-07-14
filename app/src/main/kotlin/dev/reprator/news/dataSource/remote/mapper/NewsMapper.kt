package dev.reprator.news.dataSource.remote.mapper

import dev.reprator.news.dataSource.remote.model.EntityNews
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.util.Mapper
import kotlinx.datetime.Clock
import javax.inject.Inject

class NewsMapper @Inject constructor(): Mapper<EntityNews, ModalNews>{

    override fun map(from: EntityNews): ModalNews {
        return ModalNews(from.source?.name.orEmpty(), from.author.orEmpty(), from.title.orEmpty(), from.description.orEmpty(),
            from.url.orEmpty(), from.urlToImage.orEmpty(),
            from.publishedAt?.toEpochMilliseconds() ?: Clock.System.now().epochSeconds, from.content.orEmpty())
    }
}
package dev.reprator.news.dataSource.remote.mapper

import dev.reprator.news.dataSource.remote.model.EntityNews
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.util.Mapper
import kotlinx.datetime.Clock
import javax.inject.Inject

class NewsMapper @Inject constructor(): Mapper<EntityNews, ModalNews>{

    override fun map(from: EntityNews): ModalNews {
        return ModalNews(source=from.source?.name.orEmpty(), author = from.author.orEmpty(),
            title = from.title.orEmpty(), description = from.description.orEmpty(),
            url = from.url.orEmpty(), urlToImage = from.urlToImage.orEmpty(),
            publishedAt = from.publishedAt?.toEpochMilliseconds() ?: Clock.System.now().epochSeconds,
            content = from.content.orEmpty(), isBookMarked = false, category = "")
    }
}
package dev.reprator.news.data.remote.mapper

import dev.reprator.news.data.remote.model.EntityNews
import dev.reprator.news.modal.ModalNewsList
import dev.reprator.news.util.Mapper
import kotlinx.datetime.Clock
import javax.inject.Inject

class NewsMapper @Inject constructor(): Mapper<EntityNews, ModalNewsList>{

    override fun map(from: EntityNews): ModalNewsList {
        return ModalNewsList(from.author.orEmpty(), from.title.orEmpty(), from.description.orEmpty(),
            from.url.orEmpty(), from.urlToImage.orEmpty(),
            from.publishedAt ?: Clock.System.now().epochSeconds, from.content.orEmpty())
    }
}
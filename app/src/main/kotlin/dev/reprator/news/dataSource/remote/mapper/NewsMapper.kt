package dev.reprator.news.dataSource.remote.mapper

import dev.reprator.news.dataSource.remote.model.EntityNews
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.modal.ModalNewsId
import dev.reprator.news.modal.ModalNewsPersonalisation
import dev.reprator.news.util.MapperAddition
import kotlinx.datetime.Clock
import javax.inject.Inject

class NewsMapper @Inject constructor() : MapperAddition<EntityNews, String, ModalNews> {

    override fun map(from: EntityNews, supportingInput: String): ModalNews {
        val modalNewsId =
            ModalNewsId(from.source?.name.orEmpty(), from.author.orEmpty(), from.title.orEmpty())

        val personalisation = ModalNewsPersonalisation()

        return ModalNews(
            id = modalNewsId,
            description = from.description.orEmpty(),
            url = from.url.orEmpty(),
            urlToImage = from.urlToImage.orEmpty(),
            publishedAt = from.publishedAt?.toEpochMilliseconds()
                ?: Clock.System.now().epochSeconds,
            content = from.content.orEmpty(),
            personalisation = personalisation,
            category = supportingInput
        )
    }
}
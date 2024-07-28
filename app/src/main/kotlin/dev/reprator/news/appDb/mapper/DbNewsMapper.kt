package dev.reprator.news.appDb.mapper

import dev.reprator.news.appDb.model.EntityDBNews
import dev.reprator.news.appDb.model.EntityDBNewsId
import dev.reprator.news.appDb.model.EntityDBNewsPersonalisation
import dev.reprator.news.dataSource.remote.model.EntityNews
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.modal.ModalNewsId
import dev.reprator.news.modal.ModalNewsPersonalisation
import dev.reprator.news.util.Mapper
import dev.reprator.news.util.MapperAddition
import dev.reprator.news.util.MapperToFrom
import kotlinx.datetime.Clock
import javax.inject.Inject

class DbNewsMapper @Inject constructor(): MapperToFrom<EntityDBNews, ModalNews>  {

     override fun mapTo(from: EntityDBNews): ModalNews {
        val modalNewsId =
            ModalNewsId(from.id.source, from.id.author, from.id.title)

        val personalisation = ModalNewsPersonalisation(from.personalisation.isBookMarked)

        return ModalNews(
            id = modalNewsId,
            description = from.description,
            url = from.url,
            urlToImage = from.urlToImage,
            publishedAt = from.publishedAt,
            content = from.content,
            personalisation = personalisation,
            category = from.category
        )
    }

    override fun mapFrom(from: ModalNews): EntityDBNews {
        val modalNewsId =
            EntityDBNewsId(from.id.source, from.id.author, from.id.title)

        val personalisation = EntityDBNewsPersonalisation(from.personalisation.isBookMarked)

        return EntityDBNews(
            id = modalNewsId,
            description = from.description,
            url = from.url,
            urlToImage = from.urlToImage,
            publishedAt = from.publishedAt,
            content = from.content,
            personalisation = personalisation,
            category = from.category
        )
    }
}


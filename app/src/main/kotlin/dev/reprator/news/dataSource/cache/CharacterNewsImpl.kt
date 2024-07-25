package dev.reprator.news.dataSource.cache

import dev.reprator.news.appDb.model.EntityDBNewsId
import dev.reprator.news.modal.ModalNewsPersonalisation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class NewsCacheImpl @Inject constructor() : NewsCache {

    private val cachedNewsPersonalisationStream =
        MutableStateFlow<Map<EntityDBNewsId, ModalNewsPersonalisation>>(emptyMap())

    override fun getNewsPersonalisationStream(): Flow<Map<EntityDBNewsId, ModalNewsPersonalisation>> {
        return cachedNewsPersonalisationStream
    }

    override fun updateNewsIsBookMarked(newsId: EntityDBNewsId, isBookMarked: Boolean) {
        updateNewsCache(
            characterId = newsId,
            updatePersonalisation = { cachedPersonalisation ->
                cachedPersonalisation.copy(
                    isBookMarked = isBookMarked
                )
            },
        )
    }

    private fun updateNewsCache(
        characterId: EntityDBNewsId,
        updatePersonalisation: (ModalNewsPersonalisation) -> ModalNewsPersonalisation,
    ) {
        val characterPersonalisation =
            cachedNewsPersonalisationStream.value[characterId] ?: ModalNewsPersonalisation()
        val updatedNewsPersonalisation = updatePersonalisation(characterPersonalisation)
        cachedNewsPersonalisationStream.value += characterId to updatedNewsPersonalisation
    }
}


interface NewsCache {

    fun getNewsPersonalisationStream(): Flow<Map<EntityDBNewsId, ModalNewsPersonalisation>>

    fun updateNewsIsBookMarked(newsId: EntityDBNewsId, isBookMarked: Boolean)
}

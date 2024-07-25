package dev.reprator.news.dataSource.cache

import dev.reprator.news.modal.ModalNewsId
import dev.reprator.news.modal.ModalNewsPersonalisation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class NewsCacheImpl @Inject constructor() : NewsCache {

    private val cachedNewsPersonalisationStream =
        MutableStateFlow<Map<ModalNewsId, ModalNewsPersonalisation>>(emptyMap())

    override fun getNewsPersonalisationStream(): Flow<Map<ModalNewsId, ModalNewsPersonalisation>> {
        return cachedNewsPersonalisationStream
    }

    override fun updateNewsIsBookMarked(newsId: ModalNewsId, isBookMarked: Boolean) {
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
        characterId: ModalNewsId,
        updatePersonalisation: (ModalNewsPersonalisation) -> ModalNewsPersonalisation,
    ) {
        val characterPersonalisation =
            cachedNewsPersonalisationStream.value[characterId] ?: ModalNewsPersonalisation()
        val updatedNewsPersonalisation = updatePersonalisation(characterPersonalisation)
        cachedNewsPersonalisationStream.value += characterId to updatedNewsPersonalisation
    }
}


interface NewsCache {

    fun getNewsPersonalisationStream(): Flow<Map<ModalNewsId, ModalNewsPersonalisation>>

    fun updateNewsIsBookMarked(newsId: ModalNewsId, isBookMarked: Boolean)
}

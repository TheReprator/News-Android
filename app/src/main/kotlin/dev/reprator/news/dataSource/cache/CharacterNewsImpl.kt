package dev.reprator.news.dataSource.cache

import dev.reprator.news.dataSource.local.DBNewsPersonalisation
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.modal.ModalNewsId
import dev.reprator.news.modal.ModalNewsPersonalisation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class NewsCacheImpl : NewsCache {

    private val cachedNewsPersonalisationStream = MutableStateFlow<Map<ModalNewsId, DBNewsPersonalisation>>(emptyMap())

    override fun getNewsPersonalisationStream(): Flow<Map<ModalNewsId, DBNewsPersonalisation>> {
        return cachedNewsPersonalisationStream
    }

    override fun updateNewsIsBookMarked(characterId: ModalNewsId, isBookMarked: Boolean) {
        updateNewsCache(
            characterId = characterId,
            updatePersonalisation = { cachedViventPersonalisation -> cachedViventPersonalisation.copy(isBookMarked = isBookMarked) },
        )
    }

    private fun updateNewsCache(
        characterId: ModalNewsId,
        updatePersonalisation: (DBNewsPersonalisation) -> DBNewsPersonalisation,
    ) {
        val characterPersonalisation = cachedNewsPersonalisationStream.value[characterId] ?: DBNewsPersonalisation()
        val updatedNewsPersonalisation = updatePersonalisation(characterPersonalisation)
        cachedNewsPersonalisationStream.value += characterId to updatedNewsPersonalisation
    }
}

fun ModalNews.resolveCachedPersonalisation(cache: Map<ModalNewsId, DBNewsPersonalisation>): ModalNews {
    val cachedPersonalisation = cache[id]
    return if (cachedPersonalisation != null) {
        copy(personalisation = cachedPersonalisation!!.mergeWithPersonalisation(personalisation))
    } else {
        this
    }
}

fun DBNewsPersonalisation.mergeWithPersonalisation(personalisation: ModalNewsPersonalisation): ModalNewsPersonalisation {
    return ModalNewsPersonalisation(
        isBookMarked = isBookMarked ?: personalisation.isBookMarked
    )
}



interface NewsCache {

    fun getNewsPersonalisationStream(): Flow<Map<ModalNewsId, DBNewsPersonalisation>>

    fun updateNewsIsBookMarked(characterId: ModalNewsId, isBookMarked: Boolean)
}

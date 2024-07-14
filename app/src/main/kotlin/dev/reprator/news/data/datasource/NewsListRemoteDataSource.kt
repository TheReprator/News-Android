package dev.reprator.news.domain.repository

import dev.reprator.news.modal.ModalNewsList
import kotlinx.coroutines.flow.Flow

interface NewsListRepository {
    suspend fun getNews(): Flow<List<ModalNewsList>>
}
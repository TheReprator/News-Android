package dev.reprator.news.domain.repository

import androidx.paging.PagingData
import dev.reprator.news.modal.ModalNews
import kotlinx.coroutines.flow.Flow

interface NewsListRepository {
     fun getNews(source: String): Flow<PagingData<ModalNews>>
}
package dev.reprator.news.data.datasource

import androidx.paging.PagingData
import dev.reprator.news.modal.ModalNews
import kotlinx.coroutines.flow.Flow

interface NewsListRemoteDataSource {
     fun getNews(source: String): Flow<PagingData<ModalNews>>
}
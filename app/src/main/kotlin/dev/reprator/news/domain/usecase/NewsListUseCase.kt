package dev.reprator.news.domain.usecase

import androidx.paging.PagingData
import dev.reprator.news.domain.repository.NewsListRepository
import dev.reprator.news.modal.ModalNews
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsListUseCase @Inject constructor(private val repository: NewsListRepository) {
     operator fun invoke(source: String): Flow<PagingData<ModalNews>> = repository.getNews(source)
}
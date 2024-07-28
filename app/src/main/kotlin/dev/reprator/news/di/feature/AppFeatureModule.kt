package dev.reprator.news.di.feature

import androidx.paging.PagingConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.reprator.news.appDb.mapper.DbNewsMapper
import dev.reprator.news.appDb.model.EntityDBNews
import dev.reprator.news.dataSource.NewsRepositoryImpl
import dev.reprator.news.dataSource.cache.NewsCache
import dev.reprator.news.dataSource.cache.NewsCacheImpl
import dev.reprator.news.dataSource.remote.NewsRemoteDataSource
import dev.reprator.news.dataSource.remote.NewsRemoteDataSourceImpl
import dev.reprator.news.domain.NewsRepository
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.util.MapperToFrom
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class AppFeatureModule {

    private val PAGE_SIZE = 15

    @Provides
    fun providePagingConfig() = PagingConfig(
        pageSize = PAGE_SIZE,
        enablePlaceholders = true,
        prefetchDistance = 3 * PAGE_SIZE,
        initialLoadSize = 2 * PAGE_SIZE,
    )

    @Singleton
    @Provides
    fun provideNewsRepository(bind: NewsRepositoryImpl): NewsRepository = bind

    @Provides
    fun provideDbNewsMapper(bind: DbNewsMapper): MapperToFrom<EntityDBNews, ModalNews> = bind

    @Provides
    fun provideNewsRemoteDataSource(bind: NewsRemoteDataSourceImpl): NewsRemoteDataSource = bind

    @Provides
    fun provideNewsCache(bind: NewsCacheImpl): NewsCache = bind
}
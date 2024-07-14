package dev.reprator.news.di.featureModule

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.reprator.news.data.datasource.NewsListRemoteDataSource
import dev.reprator.news.data.repositoryImpl.NewsListDataRepositoryImpl
import dev.reprator.news.dataSource.remote.NewsListRemoteDataSourceImpl
import dev.reprator.news.domain.repository.NewsListRepository

@InstallIn(ViewModelComponent::class)
@Module
object NewsList {

    @Provides
    fun provideNewsListRepositoryImpl(bind: NewsListDataRepositoryImpl): NewsListRepository = bind

    @Provides
    fun provideNewsListRemoteDataSource(bind: NewsListRemoteDataSourceImpl): NewsListRemoteDataSource =
        bind
}
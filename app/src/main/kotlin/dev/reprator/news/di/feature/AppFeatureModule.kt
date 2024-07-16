package dev.reprator.news.di.feature

import androidx.paging.PagingConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@InstallIn(ViewModelComponent::class)
@Module
class AppFeatureModule {

    @Provides
    fun providePagingConfig() = PagingConfig(
        pageSize = 20,
        prefetchDistance = 2,
        initialLoadSize = 19
    )
}
package dev.reprator.news.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.reprator.news.appDb.AppNewsDatabase
import dev.reprator.news.di.impl.DiskExecutor
import javax.inject.Singleton

private const val APP_DB_NAME = "newsDB"

@InstallIn(SingletonComponent::class)
@Module
class DBModule {

    @Provides
    fun provideAppDatabase(
        @ApplicationContext app: Context,
        diskExecutor: DiskExecutor
    ) = Room.databaseBuilder(app, AppNewsDatabase::class.java, APP_DB_NAME)
        .setQueryExecutor(diskExecutor)
        .setTransactionExecutor(diskExecutor)
        .build()

    @Singleton
    @Provides
    fun provideNewsLocalDao(db: AppNewsDatabase) = db.getNewsDao()

    @Singleton
    @Provides
    fun provideNewsRemoteDao(db: AppNewsDatabase) = db.getRemoteKeysDao()
}
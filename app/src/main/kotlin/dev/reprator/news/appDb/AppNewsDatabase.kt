package dev.reprator.news.appDb

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.reprator.news.appDb.model.EntityDBNews
import dev.reprator.news.appDb.model.EntityDBRemoteNewsKeys

@Database(
    entities = [EntityDBNews::class, EntityDBRemoteNewsKeys::class],
    version = 1,
)
abstract class AppNewsDatabase: RoomDatabase() {
    abstract fun getNewsDao(): NewsDao
    abstract fun getRemoteKeysDao(): RemoteNewsDao
}
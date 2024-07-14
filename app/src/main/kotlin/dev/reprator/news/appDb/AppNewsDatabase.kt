package dev.reprator.news.appDb

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.reprator.news.dataSource.local.RemoteNewsKeys
import dev.reprator.news.modal.ModalNews

@Database(
    entities = [ModalNews::class, RemoteNewsKeys::class],
    version = 1,
)
abstract class AppNewsDatabase: RoomDatabase() {
    abstract fun getNewsDao(): NewsDao
    abstract fun getRemoteKeysDao(): RemoteNewsDao
}
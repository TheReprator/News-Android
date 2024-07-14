package dev.reprator.news.util

fun interface Mapper<F, T> {
    fun map(from: F): T
}

fun interface IndexedMapper<F, T> {
    fun map(index: Int, from: F): T
}

inline fun <F, T> Mapper<F, T>.mapAll(collection: Collection<F>) = collection.map { map(it) }
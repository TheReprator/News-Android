package dev.reprator.news.di.impl.retrofit

import kotlinx.serialization.Serializable

@Serializable
class EntityResponseContainer<T>(
    val status: String,
    val articles: T
)


@Serializable
class EntityError(
    val code: String,
    val message: String
)
package dev.reprator.news.impl.retrofit

import kotlinx.serialization.Serializable

@Serializable
class EntityResponseContainer<T>(
    val status: String,
    val data: T
)


@Serializable
class EntityError(
    val code: String,
    val message: String
)
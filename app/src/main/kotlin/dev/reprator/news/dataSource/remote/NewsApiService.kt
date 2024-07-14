package dev.reprator.news.data.remote

import dev.reprator.news.data.remote.model.EntityNews
import retrofit2.http.GET

/*
 * Retrofit API declaration for NIA Network API
 */
interface NewsApiService {
    @GET(value = "v2/top-headlines")
    suspend fun getHeadLines(
    ): List<EntityNews>

}
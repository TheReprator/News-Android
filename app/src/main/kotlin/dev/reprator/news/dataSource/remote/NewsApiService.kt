package dev.reprator.news.dataSource.remote

import dev.reprator.news.dataSource.remote.model.EntityNews
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET(value = "v2/top-headlines")
    suspend fun getHeadLines(
        @Query("category") sources: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
    ): List<EntityNews>

}
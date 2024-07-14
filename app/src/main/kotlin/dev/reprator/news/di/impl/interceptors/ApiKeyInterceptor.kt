package dev.reprator.news.di.impl.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(private val apiKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var original = chain.request()
        val url = original.url.newBuilder().addQueryParameter("apikey", apiKey).build()
        original = original.newBuilder().url(url).build()
        return chain.proceed(original)
    }

}
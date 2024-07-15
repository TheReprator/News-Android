package dev.reprator.news.di

import android.content.Context
import androidx.tracing.trace
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.reprator.news.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import dev.reprator.news.dataSource.remote.NewsApiService
import dev.reprator.news.di.impl.interceptors.ApiKeyInterceptor
import dev.reprator.news.di.impl.interceptors.CurlLoggerInterceptor
import dev.reprator.news.di.impl.retrofit.EnvelopeConverterFactory
import dev.reprator.news.di.impl.retrofit.ParseError
import dev.reprator.news.di.impl.retrofit.ParseErrorImpl
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Call
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

private const val BASE_URL = "https://newsapi.org"
private const val CONNECTION_TIME = 20L
private const val CACHE_SIZE = (50 * 1024 * 1024).toLong()

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    fun provideCache(file: File): Cache {
        return Cache(file, CACHE_SIZE)
    }

    @Provides
    fun provideFile(
        @ApplicationContext context: Context
    ): File {
        return File(context.cacheDir, "cache_newsApp")
    }

    @Provides
    @IntoSet
    fun provideHttpLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @IntoSet
    fun provideCurlLoggerInterceptor(bind : CurlLoggerInterceptor): Interceptor = bind

    @Provides
    @IntoSet
    fun provideApiKeyInterceptor(): Interceptor = ApiKeyInterceptor("93b69f7e2db9497cb31ffef8f97752ae")
    //fun provideApiKeyInterceptor(): Interceptor = ApiKeyInterceptor(BuildConfig.KEY_NEWS_API_ORG)

    @Provides
    fun provideOkHttpClient(
        interceptors: Set<@JvmSuppressWildcards Interceptor>,
        cache: Cache,
    ): Call.Factory = trace("AppOkHttpClient") {
        OkHttpClient.Builder()
            .apply {
                cache(cache)
                connectTimeout(CONNECTION_TIME, TimeUnit.SECONDS)
                readTimeout(CONNECTION_TIME, TimeUnit.SECONDS)
                writeTimeout(CONNECTION_TIME, TimeUnit.SECONDS)
                followRedirects(true)
                followSslRedirects(true)
                retryOnConnectionFailure(false)
                interceptors.forEach(::addInterceptor)
                connectionPool(ConnectionPool(10, 2, TimeUnit.MINUTES))
            }
            .build()
    }

    @Singleton
    @Provides
    fun createRetrofit(
        json: Json,
        okHttpClient: dagger.Lazy<Call.Factory>,
        converterFactory: EnvelopeConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .callFactory { okHttpClient.get().newCall(it) }
            .addConverterFactory(converterFactory)
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType()),
            )
            .build()
    }

    @Singleton
    @Provides
    fun createApiService(
        retrofit: Retrofit,
    ): NewsApiService = retrofit.create(NewsApiService::class.java)


    @Named("isDebug")
    @Provides
    fun provideIsDebug(): Boolean {
        return BuildConfig.DEBUG
    }

    @Provides
    fun provideParseError(bind: ParseErrorImpl): ParseError = bind
}

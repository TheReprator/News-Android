package dev.reprator.news.di

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.reprator.news.di.impl.AppCoroutineDispatchers
import dev.reprator.news.di.impl.AppCoroutineDispatchersImpl
import dev.reprator.news.di.impl.ConnectivityMonitor
import dev.reprator.news.di.impl.ConnectivityMonitorImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideAppCoroutineDispatchers(): AppCoroutineDispatchers = AppCoroutineDispatchersImpl()

    @Provides
    fun provideConnectivityMonitor(application: Application): ConnectivityMonitor {
        return ConnectivityMonitorImpl(application)
    }

    @Provides
    fun provideResources(activity: Application): Resources = activity.resources

    @Provides
    fun providesCoroutineScope(appCoroutineDispatchers: AppCoroutineDispatchers): CoroutineScope {
        return CoroutineScope(SupervisorJob() + appCoroutineDispatchers.default)
    }

    @Provides
    fun provideLifeCycle(): Lifecycle {
        return ProcessLifecycleOwner.get().lifecycle
    }
}
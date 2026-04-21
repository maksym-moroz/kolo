package com.focus.kolo

import android.content.Context
import com.focus.kolo.config.AppConfigRepository
import com.focus.kolo.config.impl.repo.AppConfigRepositoryImpl
import com.focus.kolo.config.impl.source.local.LocalAppConfigOverrideSource
import com.focus.kolo.config.impl.source.local.createLocalAppConfigOverrideSource
import com.focus.kolo.config.impl.source.remote.MockRemoteAppConfigOverrideSource
import com.focus.kolo.config.impl.source.remote.RemoteAppConfigOverrideSource
import com.focus.kolo.config.impl.usecase.ClearAppConfigOverrideUseCase
import com.focus.kolo.config.impl.usecase.ResetAppConfigOverridesUseCase
import com.focus.kolo.config.impl.usecase.UpdateAppConfigOverrideUseCase
import com.focus.kolo.store.StoreFactory
import com.focus.kolo.store.impl.DefaultStoreFactory
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.createGraphFactory

@DependencyGraph
interface AndroidAppGraph : DebugMenuGraph {
    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides applicationContext: Context): AndroidAppGraph
    }

    @Provides
    fun provideStoreFactory(factory: DefaultStoreFactory): StoreFactory = factory

    @Provides
    fun provideAppConfigRepo(repo: AppConfigRepositoryImpl): AppConfigRepository = repo

    @Provides
    fun remoteOverrides(source: MockRemoteAppConfigOverrideSource): RemoteAppConfigOverrideSource = source

    @Provides
    fun localOverrides(@Provides applicationContext: Context): LocalAppConfigOverrideSource =
        createLocalAppConfigOverrideSource(applicationContext)
}

fun createAndroidAppGraph(context: Context): AndroidAppGraph = createGraphFactory<AndroidAppGraph.Factory>()
    .create(context)

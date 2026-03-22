package com.focus.kolo

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
interface IosAppGraph : AppGraph {
    val updateAppConfigOverrideUseCase: UpdateAppConfigOverrideUseCase
    val clearAppConfigOverrideUseCase: ClearAppConfigOverrideUseCase
    val resetAppConfigOverridesUseCase: ResetAppConfigOverridesUseCase

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(): IosAppGraph
    }

    @Provides
    fun provideStoreFactory(factory: DefaultStoreFactory): StoreFactory = factory

    @Provides
    fun provideAppConfigRepo(repo: AppConfigRepositoryImpl): AppConfigRepository = repo

    @Provides
    fun remoteOverrides(source: MockRemoteAppConfigOverrideSource): RemoteAppConfigOverrideSource = source

    @Provides
    fun localOverrides(): LocalAppConfigOverrideSource = createLocalAppConfigOverrideSource()
}

fun createIosAppGraph(): IosAppGraph = createGraphFactory<IosAppGraph.Factory>().create()

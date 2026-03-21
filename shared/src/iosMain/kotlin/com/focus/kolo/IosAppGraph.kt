package com.focus.kolo

import com.focus.kolo.store.StoreFactory
import com.focus.kolo.store.impl.DefaultStoreFactory
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.createGraph

@DependencyGraph
interface IosAppGraph : AppGraph {
    @Provides
    fun providePlatform(): Platform = getPlatform()

    @Provides
    fun provideStoreFactory(factory: DefaultStoreFactory): StoreFactory = factory
}

actual fun createPlatformAppGraph(): AppGraph = createGraph<IosAppGraph>()

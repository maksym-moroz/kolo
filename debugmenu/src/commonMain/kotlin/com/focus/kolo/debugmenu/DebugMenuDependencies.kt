package com.focus.kolo.debugmenu

import com.focus.kolo.DebugMenuGraph
import com.focus.kolo.config.AppConfigRepository
import com.focus.kolo.config.impl.usecase.ClearAppConfigOverrideUseCase
import com.focus.kolo.config.impl.usecase.ResetAppConfigOverridesUseCase
import com.focus.kolo.config.impl.usecase.UpdateAppConfigOverrideUseCase
import com.focus.kolo.store.StoreFactory

internal interface DebugMenuDependencies {
    val storeFactory: StoreFactory
    val appConfigRepository: AppConfigRepository
    val updateAppConfigOverrideUseCase: UpdateAppConfigOverrideUseCase
    val clearAppConfigOverrideUseCase: ClearAppConfigOverrideUseCase
    val resetAppConfigOverridesUseCase: ResetAppConfigOverridesUseCase
}

internal fun DebugMenuGraph.asDebugMenuDependencies(): DebugMenuDependencies = object : DebugMenuDependencies {
    override val storeFactory: StoreFactory = this@asDebugMenuDependencies.storeFactory
    override val appConfigRepository: AppConfigRepository = this@asDebugMenuDependencies.appConfigRepository
    override val updateAppConfigOverrideUseCase: UpdateAppConfigOverrideUseCase =
        this@asDebugMenuDependencies.updateAppConfigOverrideUseCase
    override val clearAppConfigOverrideUseCase: ClearAppConfigOverrideUseCase =
        this@asDebugMenuDependencies.clearAppConfigOverrideUseCase
    override val resetAppConfigOverridesUseCase: ResetAppConfigOverridesUseCase =
        this@asDebugMenuDependencies.resetAppConfigOverridesUseCase
}

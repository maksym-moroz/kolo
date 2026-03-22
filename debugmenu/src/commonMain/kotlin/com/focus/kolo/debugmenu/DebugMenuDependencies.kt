package com.focus.kolo.debugmenu

import com.focus.kolo.config.AppConfigRepository
import com.focus.kolo.config.impl.usecase.ClearAppConfigOverrideUseCase
import com.focus.kolo.config.impl.usecase.ResetAppConfigOverridesUseCase
import com.focus.kolo.config.impl.usecase.UpdateAppConfigOverrideUseCase
import com.focus.kolo.store.StoreFactory

interface DebugMenuDependencies {
    val storeFactory: StoreFactory
    val appConfigRepository: AppConfigRepository
    val updateAppConfigOverrideUseCase: UpdateAppConfigOverrideUseCase
    val clearAppConfigOverrideUseCase: ClearAppConfigOverrideUseCase
    val resetAppConfigOverridesUseCase: ResetAppConfigOverridesUseCase
}

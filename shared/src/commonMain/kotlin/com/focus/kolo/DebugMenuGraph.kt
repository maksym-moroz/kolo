package com.focus.kolo

import com.focus.kolo.config.impl.usecase.ClearAppConfigOverrideUseCase
import com.focus.kolo.config.impl.usecase.ResetAppConfigOverridesUseCase
import com.focus.kolo.config.impl.usecase.UpdateAppConfigOverrideUseCase

interface DebugMenuGraph : AppGraph {
    val updateAppConfigOverrideUseCase: UpdateAppConfigOverrideUseCase
    val clearAppConfigOverrideUseCase: ClearAppConfigOverrideUseCase
    val resetAppConfigOverridesUseCase: ResetAppConfigOverridesUseCase
}

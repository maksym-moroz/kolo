package com.focus.kolo.config.impl.usecase

import com.focus.kolo.config.impl.override.AppConfigOverrideField
import com.focus.kolo.config.impl.source.local.LocalAppConfigOverrideSource
import dev.zacsweers.metro.Inject

@Inject
class ClearAppConfigOverrideUseCase(
    private val localAppConfigOverrideSource: LocalAppConfigOverrideSource
) {
    suspend operator fun invoke(field: AppConfigOverrideField) {
        localAppConfigOverrideSource.clear(field)
    }
}

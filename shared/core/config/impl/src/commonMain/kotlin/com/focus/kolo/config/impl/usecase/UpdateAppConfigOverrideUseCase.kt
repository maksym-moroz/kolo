package com.focus.kolo.config.impl.usecase

import com.focus.kolo.config.impl.override.AppConfigOverrideEffect
import com.focus.kolo.config.impl.source.local.LocalAppConfigOverrideSource
import dev.zacsweers.metro.Inject

@Inject
class UpdateAppConfigOverrideUseCase(
    private val localAppConfigOverrideSource: LocalAppConfigOverrideSource
) {
    suspend operator fun invoke(mutation: AppConfigOverrideEffect) {
        localAppConfigOverrideSource.update(mutation)
    }
}

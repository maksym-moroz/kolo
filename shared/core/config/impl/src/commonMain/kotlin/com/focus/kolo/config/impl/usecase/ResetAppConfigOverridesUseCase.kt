package com.focus.kolo.config.impl.usecase

import com.focus.kolo.config.impl.source.local.LocalAppConfigOverrideSource
import dev.zacsweers.metro.Inject

@Inject
class ResetAppConfigOverridesUseCase(
    private val localAppConfigOverrideSource: LocalAppConfigOverrideSource
) {
    suspend operator fun invoke() {
        localAppConfigOverrideSource.reset()
    }
}

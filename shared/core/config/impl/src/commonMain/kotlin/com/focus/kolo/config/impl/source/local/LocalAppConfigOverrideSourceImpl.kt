package com.focus.kolo.config.impl.source.local

import com.focus.kolo.config.impl.override.AppConfigOverride
import com.focus.kolo.config.impl.override.AppConfigOverrideEffect
import com.focus.kolo.config.impl.override.AppConfigOverrideField
import com.focus.kolo.config.impl.override.applyEffect
import com.focus.kolo.config.impl.override.clear
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@Inject
class LocalAppConfigOverrideSourceImpl : LocalAppConfigOverrideSource {
    override val overrides = MutableStateFlow(AppConfigOverride())

    override suspend fun update(mutation: AppConfigOverrideEffect) {
        overrides
            .update { current -> current.applyEffect(mutation) }
    }

    override suspend fun clear(field: AppConfigOverrideField) {
        overrides
            .update { current -> current.clear(field) }
    }

    override suspend fun reset() {
        overrides.value = AppConfigOverride()
    }
}

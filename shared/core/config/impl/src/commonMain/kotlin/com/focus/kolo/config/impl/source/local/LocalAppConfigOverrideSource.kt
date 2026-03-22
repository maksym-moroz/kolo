package com.focus.kolo.config.impl.source.local

import com.focus.kolo.config.impl.override.AppConfigOverride
import com.focus.kolo.config.impl.override.AppConfigOverrideEffect
import com.focus.kolo.config.impl.override.AppConfigOverrideField
import kotlinx.coroutines.flow.Flow

interface LocalAppConfigOverrideSource {
    val overrides: Flow<AppConfigOverride>

    suspend fun update(mutation: AppConfigOverrideEffect)

    suspend fun clear(field: AppConfigOverrideField)

    suspend fun reset()
}

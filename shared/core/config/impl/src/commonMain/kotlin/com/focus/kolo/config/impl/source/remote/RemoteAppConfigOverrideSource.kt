package com.focus.kolo.config.impl.source.remote

import com.focus.kolo.config.impl.override.AppConfigOverride
import kotlinx.coroutines.flow.Flow

interface RemoteAppConfigOverrideSource {
    val overrides: Flow<AppConfigOverride>
}

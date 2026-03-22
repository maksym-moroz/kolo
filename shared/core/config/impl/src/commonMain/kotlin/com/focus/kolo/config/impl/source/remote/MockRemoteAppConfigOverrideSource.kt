package com.focus.kolo.config.impl.source.remote

import com.focus.kolo.config.impl.override.AppConfigOverride
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableStateFlow

@Inject
class MockRemoteAppConfigOverrideSource : RemoteAppConfigOverrideSource {
    override val overrides = MutableStateFlow(AppConfigOverride())
}

package com.focus.kolo.config.impl.repo

import com.focus.kolo.config.AppConfigRepository
import com.focus.kolo.config.AppConfigSnapshot
import com.focus.kolo.config.AppConfigSourceState
import com.focus.kolo.config.impl.override.applyOverride
import com.focus.kolo.config.impl.override.isEmpty
import com.focus.kolo.config.impl.source.local.LocalAppConfigOverrideSource
import com.focus.kolo.config.impl.source.remote.RemoteAppConfigOverrideSource
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.combine

@Inject
class AppConfigRepositoryImpl(
    private val appConfigSource: AppConfigSource,
    remoteAppConfigOverrideSource: RemoteAppConfigOverrideSource,
    localAppConfigOverrideSource: LocalAppConfigOverrideSource
) : AppConfigRepository {
    override val snapshot = combine(
        remoteAppConfigOverrideSource.overrides,
        localAppConfigOverrideSource.overrides
    ) { remoteOverride, localOverride ->
        AppConfigSnapshot(
            config = appConfigSource.config
                .applyOverride(remoteOverride)
                .applyOverride(localOverride),
            sourceState = AppConfigSourceState(
                hasRemoteOverrides = !remoteOverride.isEmpty,
                hasLocalOverrides = !localOverride.isEmpty
            )
        )
    }
}

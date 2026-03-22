package com.focus.kolo.config

import com.focus.kolo.config.impl.override.AppConfigOverride
import com.focus.kolo.config.impl.override.AppConfigOverrideEffect
import com.focus.kolo.config.impl.override.AppFeatureFlagKey
import com.focus.kolo.config.impl.override.AppFeatureFlagsOverride
import com.focus.kolo.config.impl.repo.AppConfigRepositoryImpl
import com.focus.kolo.config.impl.repo.AppConfigSource
import com.focus.kolo.config.impl.source.local.LocalAppConfigOverrideSourceImpl
import com.focus.kolo.config.impl.source.remote.RemoteAppConfigOverrideSource
import com.focus.kolo.config.impl.usecase.UpdateAppConfigOverrideUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout

class AppConfigRepositoryImplTest {
    @Test
    fun `local override wins over remote override`() = runTest {
        val localOverrides = LocalAppConfigOverrideSourceImpl()
        val remoteOverrides = FakeRemoteAppConfigOverrideSource(
            AppConfigOverride(
                featureFlags = AppFeatureFlagsOverride(forcedUpdateEnabled = true)
            )
        )
        localOverrides.update(
            AppConfigOverrideEffect.SetFeatureFlag(
                key = AppFeatureFlagKey.FORCED_UPDATE_ENABLED,
                enabled = false
            )
        )
        val repo = AppConfigRepositoryImpl(AppConfigSource(), remoteOverrides, localOverrides)

        assertEquals(
            false,
            repo.snapshot
                .first()
                .config.featureFlags.forcedUpdateEnabled
        )
    }

    @Test
    fun `repo restores existing local overrides on startup`() = runTest {
        val localOverrides = LocalAppConfigOverrideSourceImpl()
        localOverrides
            .update(
                AppConfigOverrideEffect
                    .SetEnvironment(AppEnvironment.DEV)
            )

        val repo = AppConfigRepositoryImpl(
            AppConfigSource(),
            FakeRemoteAppConfigOverrideSource(),
            localOverrides
        )

        val environment = withTimeout(5_000) {
            repo.snapshot
                .map { snapshot -> snapshot.config.environment }
                .first { value -> value == AppEnvironment.DEV }
        }

        assertEquals(AppEnvironment.DEV, environment)
    }

    @Test
    fun `non environment changes apply immediately`() = runTest {
        val localOverrides = LocalAppConfigOverrideSourceImpl()
        val updateUseCase = UpdateAppConfigOverrideUseCase(localOverrides)

        updateUseCase(
            AppConfigOverrideEffect
                .SetLatestVersion("1.2.3")
        )

        val latestVersion =
            localOverrides.overrides
                .first()
                .versionPolicy
                ?.latestVersion

        assertEquals("1.2.3", latestVersion)
    }

    @Test
    fun `snapshot exposes active override layers`() = runTest {
        val localOverrides = LocalAppConfigOverrideSourceImpl()
        localOverrides
            .update(
                AppConfigOverrideEffect
                    .SetEnvironment(AppEnvironment.DEV)
            )
        val remoteOverrides = FakeRemoteAppConfigOverrideSource(
            AppConfigOverride(
                featureFlags = AppFeatureFlagsOverride(forcedUpdateEnabled = true)
            )
        )
        val repo = AppConfigRepositoryImpl(AppConfigSource(), remoteOverrides, localOverrides)

        val sourceState =
            repo.snapshot
                .first()
                .sourceState

        assertEquals(true, sourceState.hasRemoteOverrides)
        assertEquals(true, sourceState.hasLocalOverrides)
        assertEquals(
            listOf(
                AppConfigSourceLayer.BUNDLED,
                AppConfigSourceLayer.REMOTE,
                AppConfigSourceLayer.LOCAL
            ),
            sourceState.activeLayers
        )
    }

    @Test
    fun `snapshot falls back to bundled layer when overrides are cleared`() = runTest {
        val localOverrides = LocalAppConfigOverrideSourceImpl()
        localOverrides
            .update(
                AppConfigOverrideEffect
                    .SetEnvironment(AppEnvironment.DEV)
            )
        val remoteOverrides = FakeRemoteAppConfigOverrideSource(
            AppConfigOverride(
                featureFlags = AppFeatureFlagsOverride(forcedUpdateEnabled = true)
            )
        )
        val repo = AppConfigRepositoryImpl(AppConfigSource(), remoteOverrides, localOverrides)

        remoteOverrides
            .update(AppConfigOverride())
        localOverrides
            .reset()

        val sourceState = withTimeout(5_000) {
            repo.snapshot
                .map { snapshot -> snapshot.sourceState }
                .first { value -> value == AppConfigSourceState() }
        }

        assertEquals(AppConfigSourceState(), sourceState)
    }

    @Test
    fun `switching environment preserves other overrides`() = runTest {
        val localOverrides = LocalAppConfigOverrideSourceImpl()
        val updateUseCase = UpdateAppConfigOverrideUseCase(localOverrides)

        updateUseCase(
            AppConfigOverrideEffect.SetFeatureFlag(
                key = AppFeatureFlagKey.REMINDERS_ENABLED,
                enabled = false
            )
        )
        updateUseCase(
            AppConfigOverrideEffect
                .SetEnvironment(AppEnvironment.DEV)
        )

        val override =
            localOverrides.overrides
                .first()

        assertEquals(AppEnvironment.DEV, override.environment)
        assertEquals(
            false,
            override.featureFlags
                ?.remindersEnabled
        )
    }

    private class FakeRemoteAppConfigOverrideSource(
        initialOverride: AppConfigOverride = AppConfigOverride()
    ) : RemoteAppConfigOverrideSource {
        private val mutableOverrides = MutableStateFlow(initialOverride)

        override val overrides: Flow<AppConfigOverride> = mutableOverrides

        fun update(override: AppConfigOverride) {
            mutableOverrides
                .update { override }
        }
    }
}

package com.focus.kolo.debugmenu

import com.focus.kolo.config.AppEnvironment
import com.focus.kolo.config.impl.override.AppConfigOverrideField
import com.focus.kolo.config.impl.override.AppFeatureFlagKey
import com.focus.kolo.store.UiIntent

sealed interface DebugMenuIntent : UiIntent {
    data class SetFeatureFlag(
        val key: AppFeatureFlagKey,
        val enabled: Boolean
    ) : DebugMenuIntent

    data class SetLatestVersion(
        val value: String
    ) : DebugMenuIntent

    data class SetMinimumSupportedVersion(
        val value: String
    ) : DebugMenuIntent

    data class SetEnvironment(
        val environment: AppEnvironment
    ) : DebugMenuIntent

    data class ClearOverride(
        val field: AppConfigOverrideField
    ) : DebugMenuIntent

    data object ResetAllOverrides : DebugMenuIntent

    data object Close : DebugMenuIntent
}

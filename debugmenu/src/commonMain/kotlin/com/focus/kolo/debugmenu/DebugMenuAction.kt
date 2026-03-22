package com.focus.kolo.debugmenu

import com.focus.kolo.config.AppConfigSnapshot
import com.focus.kolo.config.AppEnvironment
import com.focus.kolo.config.impl.override.AppConfigOverrideField
import com.focus.kolo.config.impl.override.AppFeatureFlagKey
import com.focus.kolo.store.UiAction

sealed interface DebugMenuAction : UiAction {
    sealed interface IntentAction : DebugMenuAction {
        data object Close : IntentAction

        data class SetFeatureFlag(
            val key: AppFeatureFlagKey,
            val enabled: Boolean
        ) : IntentAction

        data class SetLatestVersion(
            val value: String
        ) : IntentAction

        data class SetMinimumSupportedVersion(
            val value: String
        ) : IntentAction

        data class SetEnvironment(
            val environment: AppEnvironment
        ) : IntentAction

        data class ClearOverride(
            val field: AppConfigOverrideField
        ) : IntentAction

        data object ResetAllOverrides : IntentAction
    }

    sealed interface SystemAction : DebugMenuAction {
        data class ConfigObserved(
            val snapshot: AppConfigSnapshot
        ) : SystemAction

        data object CommandSucceeded : SystemAction

        data class CommandFailed(
            val message: String
        ) : SystemAction
    }
}

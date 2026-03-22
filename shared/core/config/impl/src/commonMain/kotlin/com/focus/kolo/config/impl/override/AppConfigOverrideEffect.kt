package com.focus.kolo.config.impl.override

import com.focus.kolo.config.AppEnvironment

sealed interface AppConfigOverrideEffect {
    data class SetEnvironment(
        val value: AppEnvironment
    ) : AppConfigOverrideEffect

    data class SetFeatureFlag(
        val key: AppFeatureFlagKey,
        val enabled: Boolean
    ) : AppConfigOverrideEffect

    data class SetLatestVersion(
        val value: String
    ) : AppConfigOverrideEffect

    data class SetMinimumSupportedVersion(
        val value: String
    ) : AppConfigOverrideEffect

    data class SetSupportUrl(
        val value: String
    ) : AppConfigOverrideEffect

    data class SetPrivacyPolicyUrl(
        val value: String
    ) : AppConfigOverrideEffect
}

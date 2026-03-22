package com.focus.kolo.config.impl.override

fun AppConfigOverride.applyEffect(mutation: AppConfigOverrideEffect): AppConfigOverride = when (mutation) {
    is AppConfigOverrideEffect.SetEnvironment -> {
        copy(environment = mutation.value)
    }
    is AppConfigOverrideEffect.SetFeatureFlag -> {
        copy(
            featureFlags = (featureFlags ?: AppFeatureFlagsOverride()).applyEffect(
                key = mutation.key,
                enabled = mutation.enabled
            )
        )
    }
    is AppConfigOverrideEffect.SetLatestVersion -> {
        copy(
            versionPolicy = (versionPolicy ?: AppVersionPolicyOverride()).copy(
                latestVersion = mutation.value
            )
        )
    }
    is AppConfigOverrideEffect.SetMinimumSupportedVersion -> {
        copy(
            versionPolicy = (versionPolicy ?: AppVersionPolicyOverride()).copy(
                minimumSupportedVersion = mutation.value
            )
        )
    }
    is AppConfigOverrideEffect.SetSupportUrl -> {
        copy(
            urls = (urls ?: AppUrlsOverride()).copy(
                supportUrl = mutation.value
            )
        )
    }
    is AppConfigOverrideEffect.SetPrivacyPolicyUrl -> {
        copy(
            urls = (urls ?: AppUrlsOverride()).copy(
                privacyPolicyUrl = mutation.value
            )
        )
    }
}

private fun AppFeatureFlagsOverride.applyEffect(
    key: AppFeatureFlagKey,
    enabled: Boolean
): AppFeatureFlagsOverride = when (key) {
    AppFeatureFlagKey.TASKS_ENABLED -> {
        copy(tasksEnabled = enabled)
    }
    AppFeatureFlagKey.JOURNAL_ENABLED -> {
        copy(journalEnabled = enabled)
    }
    AppFeatureFlagKey.REMINDERS_ENABLED -> {
        copy(remindersEnabled = enabled)
    }
    AppFeatureFlagKey.FORCED_UPDATE_ENABLED -> {
        copy(forcedUpdateEnabled = enabled)
    }
    AppFeatureFlagKey.DEEPLINK_HANDLING_ENABLED -> {
        copy(deeplinkHandlingEnabled = enabled)
    }
}

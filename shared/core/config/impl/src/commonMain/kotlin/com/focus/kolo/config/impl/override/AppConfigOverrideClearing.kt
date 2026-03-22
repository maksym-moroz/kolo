package com.focus.kolo.config.impl.override

fun AppConfigOverride.clear(field: AppConfigOverrideField): AppConfigOverride = when {
    field == AppConfigOverrideField.ENVIRONMENT -> {
        copy(environment = null)
    }
    featureFlagClearers.containsKey(field) -> {
        clearFeatureFlag(featureFlagClearers.getValue(field))
    }
    versionPolicyClearers.contains(field) -> {
        clearVersionPolicy(field)
    }
    else -> {
        clearUrl(field)
    }
}

private fun AppConfigOverride.clearFeatureFlag(key: AppFeatureFlagKey): AppConfigOverride = copy(
    featureFlags = (featureFlags ?: AppFeatureFlagsOverride()).clear(key)
)

private fun AppFeatureFlagsOverride.clear(key: AppFeatureFlagKey): AppFeatureFlagsOverride = when (key) {
    AppFeatureFlagKey.TASKS_ENABLED -> {
        copy(tasksEnabled = null)
    }
    AppFeatureFlagKey.JOURNAL_ENABLED -> {
        copy(journalEnabled = null)
    }
    AppFeatureFlagKey.REMINDERS_ENABLED -> {
        copy(remindersEnabled = null)
    }
    AppFeatureFlagKey.FORCED_UPDATE_ENABLED -> {
        copy(forcedUpdateEnabled = null)
    }
    AppFeatureFlagKey.DEEPLINK_HANDLING_ENABLED -> {
        copy(deeplinkHandlingEnabled = null)
    }
}

private val featureFlagClearers = mapOf(
    AppConfigOverrideField.TASKS_ENABLED to AppFeatureFlagKey.TASKS_ENABLED,
    AppConfigOverrideField.JOURNAL_ENABLED to AppFeatureFlagKey.JOURNAL_ENABLED,
    AppConfigOverrideField.REMINDERS_ENABLED to AppFeatureFlagKey.REMINDERS_ENABLED,
    AppConfigOverrideField.FORCED_UPDATE_ENABLED to AppFeatureFlagKey.FORCED_UPDATE_ENABLED,
    AppConfigOverrideField.DEEPLINK_HANDLING_ENABLED to AppFeatureFlagKey.DEEPLINK_HANDLING_ENABLED
)

private val versionPolicyClearers = setOf(
    AppConfigOverrideField.LATEST_VERSION,
    AppConfigOverrideField.MINIMUM_SUPPORTED_VERSION
)

private fun AppConfigOverride.clearVersionPolicy(field: AppConfigOverrideField): AppConfigOverride = copy(
    versionPolicy = when (field) {
        AppConfigOverrideField.LATEST_VERSION -> {
            (versionPolicy ?: AppVersionPolicyOverride()).copy(latestVersion = null)
        }
        AppConfigOverrideField.MINIMUM_SUPPORTED_VERSION -> {
            (versionPolicy ?: AppVersionPolicyOverride()).copy(minimumSupportedVersion = null)
        }
        else -> {
            versionPolicy ?: AppVersionPolicyOverride()
        }
    }
)

private fun AppConfigOverride.clearUrl(field: AppConfigOverrideField): AppConfigOverride = copy(
    urls = when (field) {
        AppConfigOverrideField.SUPPORT_URL -> {
            (urls ?: AppUrlsOverride()).copy(supportUrl = null)
        }
        AppConfigOverrideField.PRIVACY_POLICY_URL -> {
            (urls ?: AppUrlsOverride()).copy(privacyPolicyUrl = null)
        }
        else -> {
            urls ?: AppUrlsOverride()
        }
    }
)

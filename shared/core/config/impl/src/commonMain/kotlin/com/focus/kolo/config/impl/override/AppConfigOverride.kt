package com.focus.kolo.config.impl.override

import com.focus.kolo.config.AppEnvironment

data class AppConfigOverride(
    val environment: AppEnvironment? = null,
    val featureFlags: AppFeatureFlagsOverride? = null,
    val versionPolicy: AppVersionPolicyOverride? = null,
    val urls: AppUrlsOverride? = null
)

val AppConfigOverride.isEmpty: Boolean
    get() = environment == null &&
        (featureFlags == null || featureFlags.isEmpty) &&
        (versionPolicy == null || versionPolicy.isEmpty) &&
        (urls == null || urls.isEmpty)

private val AppFeatureFlagsOverride.isEmpty: Boolean
    get() = tasksEnabled == null &&
        journalEnabled == null &&
        remindersEnabled == null &&
        forcedUpdateEnabled == null &&
        deeplinkHandlingEnabled == null

private val AppVersionPolicyOverride.isEmpty: Boolean
    get() = latestVersion == null && minimumSupportedVersion == null

private val AppUrlsOverride.isEmpty: Boolean
    get() = supportUrl == null && privacyPolicyUrl == null

package com.focus.kolo.config.impl.override

import com.focus.kolo.config.AppConfig
import com.focus.kolo.config.AppFeatureFlags
import com.focus.kolo.config.AppUrls
import com.focus.kolo.config.AppVersionPolicy

fun AppConfig.applyOverride(override: AppConfigOverride): AppConfig = copy(
    environment = override.environment ?: environment,
    featureFlags = featureFlags.copy(
        tasksEnabled = override.featureFlags
            ?.tasksEnabled ?: featureFlags.tasksEnabled,
        journalEnabled = override.featureFlags
            ?.journalEnabled ?: featureFlags.journalEnabled,
        remindersEnabled = override.featureFlags
            ?.remindersEnabled ?: featureFlags.remindersEnabled,
        forcedUpdateEnabled = override.featureFlags
            ?.forcedUpdateEnabled ?: featureFlags.forcedUpdateEnabled,
        deeplinkHandlingEnabled = override.featureFlags
            ?.deeplinkHandlingEnabled ?: featureFlags.deeplinkHandlingEnabled
    ),
    versionPolicy = versionPolicy.copy(
        latestVersion = override.versionPolicy
            ?.latestVersion ?: versionPolicy.latestVersion,
        minimumSupportedVersion = override.versionPolicy
            ?.minimumSupportedVersion ?: versionPolicy.minimumSupportedVersion
    ),
    urls = urls.copy(
        supportUrl = override.urls
            ?.supportUrl ?: urls.supportUrl,
        privacyPolicyUrl = override.urls
            ?.privacyPolicyUrl ?: urls.privacyPolicyUrl
    )
)

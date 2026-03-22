package com.focus.kolo.config.impl.repo

import com.focus.kolo.config.AppConfig
import com.focus.kolo.config.AppEnvironment
import com.focus.kolo.config.AppFeatureFlags
import com.focus.kolo.config.AppUrls
import com.focus.kolo.config.AppVersionPolicy

// todo(maksymmoroz) rework
object BundledAppConfig {
    val config = AppConfig(
        environment = AppEnvironment.PROD,
        featureFlags = AppFeatureFlags(
            tasksEnabled = true,
            journalEnabled = true,
            remindersEnabled = true,
            forcedUpdateEnabled = false,
            deeplinkHandlingEnabled = true
        ),
        versionPolicy = AppVersionPolicy(
            latestVersion = "0.1.0",
            minimumSupportedVersion = "0.1.0"
        ),
        urls = AppUrls(
            supportUrl = "https://example.com/kolo/support",
            privacyPolicyUrl = "https://example.com/kolo/privacy-policy"
        )
    )
}

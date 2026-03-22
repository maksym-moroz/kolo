package com.focus.kolo.config

data class AppConfig(
    val environment: AppEnvironment,
    val featureFlags: AppFeatureFlags,
    val versionPolicy: AppVersionPolicy,
    val urls: AppUrls
)

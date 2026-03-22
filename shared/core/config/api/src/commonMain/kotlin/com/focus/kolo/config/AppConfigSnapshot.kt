package com.focus.kolo.config

data class AppConfigSnapshot(
    val config: AppConfig,
    val sourceState: AppConfigSourceState = AppConfigSourceState()
)

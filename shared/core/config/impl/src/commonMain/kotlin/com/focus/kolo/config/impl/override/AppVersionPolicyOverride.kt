package com.focus.kolo.config.impl.override

data class AppVersionPolicyOverride(
    val latestVersion: String? = null,
    val minimumSupportedVersion: String? = null
)

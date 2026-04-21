package com.focus.kolo.appshell

data class AppShellDefinition(
    val id: String,
    val displayName: String,
    val packageName: String,
    val bundleId: String,
    val capabilities: List<AppCapability>
) {
    fun supports(capability: AppCapability): Boolean = capability in capabilities
}

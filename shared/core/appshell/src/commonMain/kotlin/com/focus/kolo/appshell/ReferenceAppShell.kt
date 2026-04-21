package com.focus.kolo.appshell

object ReferenceAppShell {
    // First cut: the reference shell is authoritative in code until the repo grows
    // a real app-definition loading path. The YAML stays mirrored for owner-facing docs.
    val definition: AppShellDefinition =
        AppShellDefinition(
            id = "reference",
            displayName = "Kolo",
            packageName = "com.focus.kolo",
            bundleId = "com.focus.kolo",
            capabilities =
                listOf(
                    AppCapability.Tasks,
                    AppCapability.Journal,
                    AppCapability.Reminders,
                    AppCapability.Settings,
                    AppCapability.DebugMenu
                )
        )
}

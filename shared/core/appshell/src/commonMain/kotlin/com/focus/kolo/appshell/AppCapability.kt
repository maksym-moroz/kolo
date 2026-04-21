package com.focus.kolo.appshell

enum class AppCapability(
    val wireName: String,
    val displayName: String
) {
    Tasks(
        wireName = "tasks",
        displayName = "Tasks"
    ),
    Journal(
        wireName = "journal",
        displayName = "Journal"
    ),
    Reminders(
        wireName = "reminders",
        displayName = "Reminders"
    ),
    Settings(
        wireName = "settings",
        displayName = "Settings"
    ),
    DebugMenu(
        wireName = "debugmenu",
        displayName = "Debug menu"
    )
}

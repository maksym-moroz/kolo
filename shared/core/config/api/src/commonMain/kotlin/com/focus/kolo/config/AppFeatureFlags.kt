package com.focus.kolo.config

data class AppFeatureFlags(
    val tasksEnabled: Boolean,
    val journalEnabled: Boolean,
    val remindersEnabled: Boolean,
    val forcedUpdateEnabled: Boolean,
    val deeplinkHandlingEnabled: Boolean
)

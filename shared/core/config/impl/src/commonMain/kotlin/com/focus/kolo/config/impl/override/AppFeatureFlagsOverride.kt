package com.focus.kolo.config.impl.override

data class AppFeatureFlagsOverride(
    val tasksEnabled: Boolean? = null,
    val journalEnabled: Boolean? = null,
    val remindersEnabled: Boolean? = null,
    val forcedUpdateEnabled: Boolean? = null,
    val deeplinkHandlingEnabled: Boolean? = null
)

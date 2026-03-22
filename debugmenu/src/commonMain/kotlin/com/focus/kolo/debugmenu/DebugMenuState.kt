package com.focus.kolo.debugmenu

import com.focus.kolo.config.AppConfigSnapshot
import com.focus.kolo.store.UiState

data class DebugMenuState(
    val snapshot: AppConfigSnapshot? = null,
    val commandFailureMessage: String? = null
) : UiState

package com.focus.kolo.debugmenu

import com.focus.kolo.store.UiEffect

sealed interface DebugMenuEffect : UiEffect {
    data object Close : DebugMenuEffect

    data object RestartRequired : DebugMenuEffect
}

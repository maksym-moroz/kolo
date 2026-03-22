package com.focus.kolo.debugmenu

import com.focus.kolo.store.Reducer

class DebugMenuReducer : Reducer<DebugMenuState, DebugMenuAction> {
    override fun reduce(
        current: DebugMenuState,
        action: DebugMenuAction
    ): DebugMenuState = when (action) {
        is DebugMenuAction.SystemAction.ConfigObserved -> {
            current.copy(
                snapshot = action.snapshot,
                commandFailureMessage = null
            )
        }
        DebugMenuAction.SystemAction.CommandSucceeded -> {
            current.copy(commandFailureMessage = null)
        }
        is DebugMenuAction.SystemAction.CommandFailed -> {
            current.copy(commandFailureMessage = action.message)
        }
        else -> {
            current
        }
    }
}

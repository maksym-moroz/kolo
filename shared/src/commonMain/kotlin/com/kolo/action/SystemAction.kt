package com.kolo.action

sealed interface SystemAction : Action {
    data object InitialAction : SystemAction

    data object ComponentReady : Action

    data object FinalAction : SystemAction
}

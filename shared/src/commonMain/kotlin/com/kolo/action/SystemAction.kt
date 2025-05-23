package com.kolo.action

sealed interface SystemAction : Action {
    data object InitialAction : SystemAction

    data object FinalAction : SystemAction
}

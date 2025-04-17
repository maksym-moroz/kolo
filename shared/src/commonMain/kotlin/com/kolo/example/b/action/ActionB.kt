package com.kolo.example.b.action

import com.kolo.action.Action
import com.kolo.action.AsEventAction

sealed interface ActionB : Action {
    data object Increment : ActionB

    data object Decrement : ActionB

    data object Reset : ActionB

    data object DisplaySnackbarActionB : ActionB, AsEventAction
}

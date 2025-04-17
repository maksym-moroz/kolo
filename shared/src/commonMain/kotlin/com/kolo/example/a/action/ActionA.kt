package com.kolo.example.a.action

import com.kolo.action.Action
import com.kolo.action.AsEventAction

sealed interface ActionA : Action {
    data object Increment : ActionA

    data object Decrement : ActionA

    data object Reset : ActionA

    data object DisplaySnackbarActionA : ActionA, AsEventAction
}

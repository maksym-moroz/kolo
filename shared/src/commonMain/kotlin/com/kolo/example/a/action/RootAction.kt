package com.kolo.example.a.action

import com.kolo.action.Action
import com.kolo.action.AsEventAction

sealed interface RootAction : Action {
    data object Increment : RootAction

    data object Decrement : RootAction

    data object Reset : RootAction

    data object DisplaySnackbarAction : RootAction, AsEventAction
}

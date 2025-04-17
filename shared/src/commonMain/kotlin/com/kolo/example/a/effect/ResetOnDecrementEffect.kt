package com.kolo.example.a.effect

import com.kolo.action.Action
import com.kolo.effect.Effect
import com.kolo.example.a.action.RootAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map

class ResetOnDecrementEffect : Effect {
    override fun intercept(actions: Flow<Action>): Flow<Action> =
        actions
            .filterIsInstance<RootAction.Decrement>()
            .map { RootAction.Reset }
}

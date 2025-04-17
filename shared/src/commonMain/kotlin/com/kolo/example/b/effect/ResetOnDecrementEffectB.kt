package com.kolo.example.b.effect

import com.kolo.action.Action
import com.kolo.effect.Effect
import com.kolo.example.a.action.ActionA
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map

class ResetOnDecrementEffectB : Effect {
    override fun intercept(actions: Flow<Action>): Flow<Action> =
        actions
            .filterIsInstance<ActionA.Decrement>()
            .map { ActionA.Reset }
}

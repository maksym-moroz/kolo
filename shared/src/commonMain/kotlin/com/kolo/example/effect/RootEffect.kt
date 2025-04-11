package com.kolo.example.effect

import com.kolo.action.Action
import com.kolo.action.SystemAction
import com.kolo.effect.Effect
import com.kolo.example.action.RootAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map

class RootEffect : Effect {
    override fun intercept(actions: Flow<Action>): Flow<Action> =
        actions
            .filterIsInstance<RootAction.Decrement>()
            .map { SystemAction.FinalAction }
}

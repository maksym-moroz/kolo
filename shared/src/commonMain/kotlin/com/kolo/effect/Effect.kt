package com.kolo.effect

import com.kolo.action.Action
import com.kolo.action.SystemAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map

interface Effect {
    fun intercept(actions: Flow<Action>): Flow<Action>
}

class ExampleActionEffect : Effect {
    override fun intercept(actions: Flow<Action>): Flow<Action> =
        actions
            .filterIsInstance<SystemAction.InitialAction>()
            .map { SystemAction.FinalAction }
}

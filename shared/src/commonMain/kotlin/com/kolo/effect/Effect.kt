package com.kolo.effect

import com.kolo.action.Action
import kotlinx.coroutines.flow.Flow

interface Effect {
    fun intercept(actions: Flow<Action>): Flow<Action>
}

// class ExampleActionEffect : Effect {
//    override fun intercept(actions: Flow<Action>): Flow<Action> =
//        actions
//            .filterIsInstance<SystemAction.InitialAction>()
//            .map { SystemAction.FinalAction }
// }

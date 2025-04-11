package com.kolo.store

import com.kolo.action.Action
import com.kolo.state.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface Store<S : State> {
    val scope: CoroutineScope
    val states: StateFlow<S>
    val events: SharedFlow<Action>
    val actions: SharedFlow<Action>

    suspend fun dispatch(action: Action)
}

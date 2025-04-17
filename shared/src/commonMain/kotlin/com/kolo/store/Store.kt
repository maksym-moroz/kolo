package com.kolo.store

import com.kolo.action.Action
import com.kolo.state.Contract
import com.kolo.state.Self
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface Store<S : Self, C : Contract> {
    val scope: CoroutineScope
    val states: StateFlow<S>
    val contracts: StateFlow<C>
    val events: SharedFlow<Action>
    val actions: SharedFlow<Action>

    // suspend
    fun dispatch(action: Action)
}

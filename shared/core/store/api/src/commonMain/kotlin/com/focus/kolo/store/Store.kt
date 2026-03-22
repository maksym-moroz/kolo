package com.focus.kolo.store

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface Store<S : UiState, A : UiAction, E : UiEffect> {
    val scope: CoroutineScope
    val state: StateFlow<S>
    val actions: SharedFlow<A>
    val effects: SharedFlow<E>

    suspend fun dispatch(action: A)

    suspend fun emit(effect: E)
}

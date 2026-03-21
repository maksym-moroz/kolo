package com.focus.kolo.store

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface Store<S : UiState, A : UiAction, E : UiEffect> {
    val state: StateFlow<S>
    val effects: SharedFlow<E>

    suspend fun dispatch(action: A)
}

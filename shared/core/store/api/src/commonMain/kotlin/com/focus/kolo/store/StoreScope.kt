package com.focus.kolo.store

import kotlinx.coroutines.flow.StateFlow

interface StoreScope<S : UiState, A : UiAction, E : UiEffect> {
    val state: StateFlow<S>

    val currentState: S
        get() = state.value

    suspend fun dispatch(action: A)

    suspend fun emit(effect: E)
}

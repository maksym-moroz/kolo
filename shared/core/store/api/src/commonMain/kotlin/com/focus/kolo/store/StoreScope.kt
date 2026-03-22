package com.focus.kolo.store

interface StoreScope<S : UiState, A : UiAction, E : UiEffect> : Store<S, A, E> {
    val currentState: S
        get() = state.value
}

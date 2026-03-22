package com.focus.kolo.store

fun interface Middleware<S : UiState, A : UiAction, E : UiEffect> {
    fun interfere(
        store: StoreScope<S, A, E>,
        next: Next<A>
    ): Next<A>
}

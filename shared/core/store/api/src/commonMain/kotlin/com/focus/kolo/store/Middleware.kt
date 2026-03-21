package com.focus.kolo.store

fun interface Middleware<S : UiState, A : UiAction, E : UiEffect> {
    suspend fun intercept(
        action: A,
        scope: StoreScope<S, A, E>,
        next: Next<A>,
    )
}

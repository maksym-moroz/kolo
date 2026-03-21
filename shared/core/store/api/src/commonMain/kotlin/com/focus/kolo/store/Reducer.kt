package com.focus.kolo.store

fun interface Reducer<S : UiState, A : UiAction> {
    fun reduce(current: S, action: A): S
}

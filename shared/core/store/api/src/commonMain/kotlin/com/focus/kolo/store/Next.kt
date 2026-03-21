package com.focus.kolo.store

fun interface Next<A : UiAction> {
    suspend fun dispatch(action: A)
}

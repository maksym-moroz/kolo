package com.focus.kolo.store

fun interface IntentMapper<I : UiIntent, A : UiAction> {
    fun map(intent: I): A
}

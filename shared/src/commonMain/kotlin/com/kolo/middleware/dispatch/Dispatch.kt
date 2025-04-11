package com.kolo.middleware.dispatch

import com.kolo.action.Action

fun interface Dispatch<A : Action> {
    suspend fun perform(action: A)
}

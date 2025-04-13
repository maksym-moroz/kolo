package com.kolo.middleware

import com.kolo.action.Action

fun interface Dispatch<A : Action> {
    suspend fun perform(action: A)
}

package com.kolo.reducer

import com.kolo.action.Action

fun interface Reducer<A : Action, S : Any> {
    fun reduce(
        action: A,
        state: S,
    ): S
}

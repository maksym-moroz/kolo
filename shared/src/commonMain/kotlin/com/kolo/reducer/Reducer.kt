package com.kolo.reducer

import com.kolo.action.Action
import com.kolo.state.State

fun interface Reducer<S : State> {
    fun reduce(
        state: S,
        action: Action,
    ): S
}

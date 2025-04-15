package com.kolo.reducer

import com.kolo.action.Action
import com.kolo.state.Self

fun interface Reducer<S : Self> {
    fun reduce(
        self: S,
        action: Action,
    ): S
}

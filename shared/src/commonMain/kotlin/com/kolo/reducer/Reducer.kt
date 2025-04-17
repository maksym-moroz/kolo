package com.kolo.reducer

import com.kolo.action.Action
import com.kolo.state.Contract
import com.kolo.state.Self

fun interface Reducer<S : Self> {
    fun reduce(
        self: S,
        contract: Contract,
        action: Action,
    ): S
}

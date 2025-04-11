package com.kolo.middleware

import com.kolo.action.Action
import com.kolo.middleware.dispatch.Dispatch
import com.kolo.state.State
import com.kolo.store.Store

interface Middleware<A : Action, S : State> {
    fun interfere(
        store: Store<S>,
        next: Dispatch<A>,
    ): Dispatch<A>
}

// m1 -> m2 -> m3 -x
// m3 -> m1 -> m2 -> m3 -> yes

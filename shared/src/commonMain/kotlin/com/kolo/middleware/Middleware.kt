package com.kolo.middleware

import com.kolo.action.Action
import com.kolo.state.Contract
import com.kolo.state.Self
import com.kolo.store.Store

abstract class Middleware<S : Self, C : Contract> {
    abstract fun interfere(
        store: Store<S, C>,
        next: Dispatch<Action>,
    ): Dispatch<Action>
}

// m1 -> m2 -> m3 -x
// m3 -> m1 -> m2 -> m3 -> yes

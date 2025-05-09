package com.kolo.middleware

import com.kolo.action.Action
import com.kolo.state.Self
import com.kolo.store.Store

abstract class Middleware<S : Self> {
    abstract fun interfere(
        store: Store<S>,
        next: Dispatch<Action>,
    ): Dispatch<Action>
}

// m1 -> m2 -> m3 -x
// m3 -> m1 -> m2 -> m3 -> yes

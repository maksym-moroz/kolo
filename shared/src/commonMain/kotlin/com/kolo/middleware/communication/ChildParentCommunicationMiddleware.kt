package com.kolo.middleware.communication

import com.kolo.action.Action
import com.kolo.action.ResultAction
import com.kolo.middleware.Middleware
import com.kolo.middleware.dispatch.Dispatch
import com.kolo.state.State
import com.kolo.store.Store

// todo think through possible ways to do this

class ChildParentCommunicationMiddleware<S : State> : Middleware<S>() {
    override fun interfere(
        store: Store<S>,
        next: Dispatch<Action>,
    ): Dispatch<Action> =
        Dispatch { action ->
            when (action) {
                is ResultAction -> {
                    // todo how to propagate changes?
                }
                else -> {
                    next.perform(action)
                }
            }
        }
}

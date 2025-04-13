package com.kolo.middleware.communication

import com.kolo.action.Action
import com.kolo.action.ResultAction
import com.kolo.middleware.Dispatch
import com.kolo.middleware.Middleware
import com.kolo.state.State
import com.kolo.store.Store

class ParentCommunicationMiddleware<S : State>(
    private val dispatch: ParentDispatch,
) : Middleware<S>() {
    override fun interfere(
        store: Store<S>,
        next: Dispatch<Action>,
    ): Dispatch<Action> =
        Dispatch { action ->
            when (action) {
                is ResultAction -> {
                    dispatch.dispatch(action)
                }
                else -> {
                    next.perform(action)
                }
            }
        }
}

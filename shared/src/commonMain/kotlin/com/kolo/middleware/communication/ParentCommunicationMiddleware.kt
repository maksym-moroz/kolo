package com.kolo.middleware.communication

import com.kolo.action.Action
import com.kolo.action.ResultAction
import com.kolo.middleware.Dispatch
import com.kolo.middleware.Middleware
import com.kolo.state.Contract
import com.kolo.state.Self
import com.kolo.store.Store

class ParentCommunicationMiddleware<S : Self, C : Contract>(
    private val dispatch: ParentDispatch,
) : Middleware<S, C>() {
    override fun interfere(
        store: Store<S, C>,
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

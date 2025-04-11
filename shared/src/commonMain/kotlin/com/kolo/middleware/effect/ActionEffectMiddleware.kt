package com.kolo.middleware.effect

import com.kolo.action.Action
import com.kolo.effect.Effect
import com.kolo.middleware.Middleware
import com.kolo.middleware.dispatch.Dispatch
import com.kolo.state.State
import com.kolo.store.Store
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ActionEffectMiddleware<A : Action, S : State>(
    private val effects: Set<Effect>,
) : Middleware<A, S> {
    override fun interfere(
        store: Store<S>,
        next: Dispatch<A>,
    ): Dispatch<A> {
        effects
            .forEach { sideEffect ->
                sideEffect
                    .intercept(store.actions) // can't visualize it (rework after test)
                    .onEach(store::dispatch)
                    // .catch {  } todo decide on error propagation/handling
                    .launchIn(store.scope)
            }

        return Dispatch { action ->
            next.perform(action)
        }
    }
}

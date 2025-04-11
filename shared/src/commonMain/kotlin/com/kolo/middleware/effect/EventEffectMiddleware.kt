package com.kolo.middleware.effect

import com.kolo.action.Action
import com.kolo.effect.Effect
import com.kolo.middleware.Middleware
import com.kolo.middleware.dispatch.Dispatch
import com.kolo.state.State
import com.kolo.store.Store
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EventEffectMiddleware<A : Action, S : State>(
    private val effects: Set<Effect>,
) : Middleware<A, S> {
    override fun interfere(
        store: Store<S>,
        next: Dispatch<A>,
    ): Dispatch<A> {
        effects
            .forEach { effect ->
                effect
                    .intercept(store.events) // can I merge this with action effect middleware?
                    .onEach(store::dispatch)
                    // .catch {  } todo decide on error propagation/handling
                    .launchIn(store.scope)
            }

        return Dispatch { action ->
            next.perform(action)
        }
    }
}

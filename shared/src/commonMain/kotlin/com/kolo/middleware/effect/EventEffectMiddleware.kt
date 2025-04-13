package com.kolo.middleware.effect

import com.kolo.action.Action
import com.kolo.effect.Effect
import com.kolo.middleware.Dispatch
import com.kolo.middleware.Middleware
import com.kolo.state.State
import com.kolo.store.Store
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EventEffectMiddleware<S : State>(
    private val effects: List<Effect>,
) : Middleware<S>() {
    override fun interfere(
        store: Store<S>,
        next: Dispatch<Action>,
    ): Dispatch<Action> {
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

package com.kolo.middleware.effect

import com.kolo.action.Action
import com.kolo.effect.Effect
import com.kolo.middleware.Dispatch
import com.kolo.middleware.Middleware
import com.kolo.state.Self
import com.kolo.store.Store
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ActionEffectMiddleware<S : Self>(
    private val effects: List<Effect>,
) : Middleware<S>() {
    override fun interfere(
        store: Store<S>,
        next: Dispatch<Action>,
    ): Dispatch<Action> {
        effects
            .forEach { sideEffect ->
                sideEffect
                    .intercept(store.actions)
                    .onEach(store::dispatch)
                    // .catch {  } todo decide on error propagation/handling
                    .launchIn(store.scope)
            }

        return Dispatch { action ->
            next.perform(action)
        }
    }
}

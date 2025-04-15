package com.kolo.middleware.builder

import com.kolo.effect.Effect
import com.kolo.middleware.Middleware
import com.kolo.middleware.communication.ParentCommunicationMiddleware
import com.kolo.middleware.communication.ParentDispatch
import com.kolo.middleware.communication.ParentDispatchImpl
import com.kolo.middleware.communication.ParentDispatchNoop
import com.kolo.middleware.effect.ActionEffectMiddleware
import com.kolo.middleware.effect.EventEffectMiddleware
import com.kolo.state.Self

class StoreMiddlewareBuilder<S : Self> {
    private var actionEffects: List<Effect> = emptyList()
    private var eventEffects: List<Effect> = emptyList()

    private var includeActionEffects = false
    private var includeEventEffects = false

    private var parentDispatch: ParentDispatch = ParentDispatchNoop
    private var enableParentDispatch: Boolean = false

    private var additionalMiddleware: List<Middleware<S>> = emptyList()

    /**
     * If you want ActionEffectMiddleware, call this with the
     * effect list and toggle it on.
     */
    fun withActionEffects(effects: List<Effect>): StoreMiddlewareBuilder<S> {
        this.actionEffects = effects
        this.includeActionEffects = true
        return this
    }

    /**
     * If you want EventEffectMiddleware, call this with the
     * effect list and toggle it on.
     */
    fun withEventEffects(effects: List<Effect>): StoreMiddlewareBuilder<S> {
        this.eventEffects = effects
        this.includeEventEffects = true
        return this
    }

    /**
     * If the store has a parent, provide the parent's dispatch here.
     * Then decide if we want the child->parent communication enabled.
     */
    fun withParentDispatch(
        // enable: Boolean,
        dispatch: ParentDispatch,
    ): StoreMiddlewareBuilder<S> {
        this.enableParentDispatch = true
        this.parentDispatch = dispatch
        return this
    }

    /**
     * Add any custom middlewares you want, for logging, analytics, or
     * specialized logic.
     */
    fun withAdditionalMiddleware(middleware: List<Middleware<S>>): StoreMiddlewareBuilder<S> {
        additionalMiddleware = middleware
        return this
    }

    // middleware order is critical, this means the logic should be lifted and isolated
    // logic should be independent of builder, possibly weights and min-heap approach

    fun build(): List<Middleware<S>> =
        buildList {
            // If the user opted into ActionEffectMiddleware
            if (includeActionEffects && actionEffects.isNotEmpty()) {
                add(ActionEffectMiddleware(actionEffects))
            }

            // If the user opted into EventEffectMiddleware
            if (includeEventEffects && eventEffects.isNotEmpty()) {
                add(EventEffectMiddleware(eventEffects))
            }

            // Child->Parent Communication (only if explicitly enabled and we have a parent dispatch)
            if (enableParentDispatch) {
                when (parentDispatch) {
                    is ParentDispatchImpl -> {
                        add(ParentCommunicationMiddleware(parentDispatch))
                    }
                    ParentDispatchNoop -> {
                        // todo do we even need this clause?
                    }
                }
            }
        }
}

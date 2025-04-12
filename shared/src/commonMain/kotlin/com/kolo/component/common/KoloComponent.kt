package com.kolo.component.common

import com.kolo.action.Action
import com.kolo.action.ResultAction
import com.kolo.component.composition.container.EffectContainer
import com.kolo.component.composition.content.UiContent
import com.kolo.component.composition.context.children.ChildrenContext
import com.kolo.component.composition.context.children.ChildrenContextDelegate
import com.kolo.component.composition.context.reduce.ReduceContext
import com.kolo.state.State

// main component to use on app level

abstract class KoloComponent<RA : ResultAction, S : State>(
    effectContainer: EffectContainer,
    initialState: S,
) : CommonComponent<RA, S>(effectContainer, initialState),
    ChildrenContext<RA, S> by ChildrenContextDelegate(emptySet()) {
    abstract val content: UiContent<S>

    final override fun processReduce(
        context: ReduceContext,
        state: S,
        action: Action,
    ): S {
        // todo add needed logic later
        return context.reduce(state, action)
    }

    abstract fun ReduceContext.reduce(
        state: S,
        action: Action,
    ): S
}

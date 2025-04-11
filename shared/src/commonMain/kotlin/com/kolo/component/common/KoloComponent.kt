package com.kolo.component.common

import com.kolo.action.Action
import com.kolo.action.ResultAction
import com.kolo.component.composition.container.EffectContainer
import com.kolo.component.composition.content.UiContent
import com.kolo.component.composition.context.children.ChildrenContext
import com.kolo.component.composition.context.children.ChildrenContextDelegate
import com.kolo.state.State

// main component to use on app level

abstract class KoloComponent<RA : ResultAction, S : State>(
    effectContainer: EffectContainer,
    initialState: S,
) : CommonComponent<RA, S>(effectContainer, initialState),
    ChildrenContext<RA, S> by ChildrenContextDelegate(emptySet()) {
    abstract val content: UiContent<S>

    // context(ReduceContext)
    final override fun processReduce(
        state: S,
        action: Action,
    ): S {
        // todo add needed logic later
        return reduce(state, action)
    }

    abstract fun /*ReduceContext.*/reduce(
        state: S,
        action: Action,
    ): S

//    // todo(maksym) figure out what to do on iOS
//    context(StoreContext)
//    @Composable
//    abstract fun render(state: S)
}

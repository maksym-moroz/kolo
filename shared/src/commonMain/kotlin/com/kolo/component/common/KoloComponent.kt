package com.kolo.component.common

import com.kolo.action.FeatureAction
import com.kolo.action.ResultAction
import com.kolo.component.composition.container.EffectContainer
import com.kolo.component.composition.context.children.ChildrenContext
import com.kolo.component.composition.context.children.ChildrenContextDelegate
import com.kolo.component.composition.context.reduce.ReduceContext
import com.kolo.state.State

// main component to use on app level

abstract class KoloComponent<FA : FeatureAction, RA : ResultAction, S : State>(
    effectContainer: EffectContainer,
    initialState: S,
) : CommonComponent<FA, RA, S>(effectContainer, initialState),
    ChildrenContext<FA, RA, S> by ChildrenContextDelegate(emptySet()) {
    final override fun ReduceContext.processReduce(
        state: S,
        action: FA,
    ): S {
        // todo add needed logic later
        return reduce(state, action)
    }

    abstract fun ReduceContext.reduce(
        state: S,
        action: FA,
    ): S

    // StoreContext
    // @Composable/SwiftUi

    // StoreContext.render(state: S) {
    //    Screen(state) {
    //      Button(onClick = dispatch(ButtonClickAction))
    //    }
    // }
}

package com.kolo.component.common

import com.kolo.action.FeatureAction
import com.kolo.action.ResultAction
import com.kolo.component.composition.container.EffectContainer
import com.kolo.component.composition.context.children.ChildrenContext
import com.kolo.component.composition.context.children.ChildrenContextDelegate
import com.kolo.state.State

// main component to use on app level

class KoloComponent<FA : FeatureAction, RA : ResultAction, S : State>(
    effectContainer: EffectContainer,
    initialState: S,
) : CommonComponent<RA, S>(effectContainer, initialState),
    ChildrenContext<FA, RA, S> by ChildrenContextDelegate(emptySet()) {
    // ReduceContext
    // ReduceContext.reduce(state: S, action: FA)

    // StoreContext
    // @Composable/SwiftUi

    // StoreContext.render(state: S) {
    //    Screen(state) {
    //      Button(onClick = dispatch(ButtonClickAction))
    //    }
    // }
}

package com.kolo.component.common

import com.kolo.action.Action
import com.kolo.action.ResultAction
import com.kolo.component.composition.container.EffectContainer
import com.kolo.component.composition.content.UiContent
import com.kolo.component.composition.context.children.ChildrenContext
import com.kolo.component.composition.context.children.ChildrenContextDelegate
import com.kolo.component.composition.context.reduce.ReduceContext
import com.kolo.state.Contract
import com.kolo.state.Self

// main component to use on app level

abstract class KoloComponent<RA : ResultAction, S : Self, C : Contract>(
    effectContainer: EffectContainer,
    initialSelf: S,
) : CommonComponent<RA, S, C>(effectContainer, initialSelf),
    ChildrenContext<RA, S, C> by ChildrenContextDelegate(emptySet()) {
    abstract val content: UiContent<S>

    final override fun process(
        context: ReduceContext,
        self: S,
        contract: C,
        action: Action,
    ): S {
        val newState = context.reduce(self, contract, action)

        // old: [h: 99, w: 99]
        // new: [h: 100, w: 99]

        // what's mapper.invoke(newState)?
        // add(ComponentA(initialState = SelfB()) {
        //    val area = it.height * it.width
        //    ContractB(area = area)
        // }

//        children
//            .forEach { (child, mapper) ->
//                child.dispatch(ContractUpdateAction<ContractB>(mapper.invoke(newState))) (StateA) -> ContractB
//            }

        return newState
    }

    abstract fun ReduceContext.reduce(
        self: S,
        contract: C,
        action: Action,
    ): S
}

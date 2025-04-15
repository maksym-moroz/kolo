package com.kolo.component.common

import com.kolo.action.Action
import com.kolo.action.ResultAction
import com.kolo.component.composition.container.EffectContainer
import com.kolo.component.composition.context.reduce.ReduceContext
import com.kolo.component.composition.node.KoloNode
import com.kolo.state.Contract
import com.kolo.state.Self

abstract class CommonComponent<RA : ResultAction, S : Self, C : Contract>(
    override val container: EffectContainer,
    // how to connect it to the store on init?
    internal val initialSelf: S,
) : KoloNode {
    abstract fun process(
        context: ReduceContext,
        self: S,
        action: Action,
    ): S
}

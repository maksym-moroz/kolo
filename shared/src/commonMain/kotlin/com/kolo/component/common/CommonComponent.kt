package com.kolo.component.common

import com.kolo.action.FeatureAction
import com.kolo.action.ResultAction
import com.kolo.component.composition.container.EffectContainer
import com.kolo.component.composition.context.reduce.ReduceContext
import com.kolo.component.composition.node.KoloNode
import com.kolo.state.State

abstract class CommonComponent<FA : FeatureAction, RA : ResultAction, S : State>(
    override val container: EffectContainer,
    protected val state: S,
) : KoloNode<RA, S> {
    abstract fun ReduceContext.processReduce(
        state: S,
        action: FA,
    ): S
}

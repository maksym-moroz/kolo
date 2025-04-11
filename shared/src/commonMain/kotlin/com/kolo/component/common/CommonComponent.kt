package com.kolo.component.common

import com.kolo.action.ResultAction
import com.kolo.component.composition.container.EffectContainer
import com.kolo.component.composition.node.KoloNode
import com.kolo.state.State

open class CommonComponent<RA : ResultAction, S : State>(
    override val container: EffectContainer,
    protected val state: S,
) : KoloNode<RA, S>

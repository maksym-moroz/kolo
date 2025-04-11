package com.kolo.component.composition.node

import com.kolo.action.ResultAction
import com.kolo.component.composition.container.EffectContainer
import com.kolo.state.State

interface KoloNode<RA : ResultAction, S : State> {
    val container: EffectContainer
}

package com.kolo.component.composition.context.children

import com.kolo.action.ResultAction
import com.kolo.component.common.KoloComponent
import com.kolo.state.Contract
import com.kolo.state.Self

interface ChildrenContext<RA : ResultAction, S : Self, C : Contract> {
    // should be a stack (maybe?)
    val children: Set<KoloComponent<RA, S, C>>
}

package com.kolo.component.composition.context.children

import com.kolo.action.ResultAction
import com.kolo.component.common.KoloComponent
import com.kolo.state.State

interface ChildrenContext<RA : ResultAction, S : State> {
    // should be a stack (maybe?)
    val children: Set<KoloComponent<RA, S>>
}

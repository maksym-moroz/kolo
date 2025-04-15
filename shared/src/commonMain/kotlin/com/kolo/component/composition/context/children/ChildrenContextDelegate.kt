package com.kolo.component.composition.context.children

import com.kolo.action.ResultAction
import com.kolo.component.common.KoloComponent
import com.kolo.state.Contract
import com.kolo.state.Self

class ChildrenContextDelegate<RA : ResultAction, S : Self, C : Contract>(
    override val children: Set<KoloComponent<RA, S, C>>,
) : ChildrenContext<RA, S, C>

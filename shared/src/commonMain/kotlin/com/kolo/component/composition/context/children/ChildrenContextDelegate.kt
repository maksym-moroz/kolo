package com.kolo.component.composition.context.children

import com.kolo.action.ResultAction
import com.kolo.component.common.KoloComponent
import com.kolo.state.State

class ChildrenContextDelegate<RA : ResultAction, S : State>(
    override val children: Set<KoloComponent<RA, S>>,
) : ChildrenContext<RA, S>

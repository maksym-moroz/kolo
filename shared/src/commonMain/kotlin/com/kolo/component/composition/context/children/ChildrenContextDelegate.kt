package com.kolo.component.composition.context.children

import com.kolo.action.FeatureAction
import com.kolo.action.ResultAction
import com.kolo.component.common.KoloComponent
import com.kolo.state.State

class ChildrenContextDelegate<FA : FeatureAction, RA : ResultAction, S : State>(
    override val children: Set<KoloComponent<FA, RA, S>>,
) : ChildrenContext<FA, RA, S>

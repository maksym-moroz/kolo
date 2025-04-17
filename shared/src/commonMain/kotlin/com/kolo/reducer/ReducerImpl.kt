package com.kolo.reducer

import com.kolo.action.Action
import com.kolo.action.ResultAction
import com.kolo.component.common.KoloComponent
import com.kolo.component.composition.context.reduce.ReduceContext
import com.kolo.state.Contract
import com.kolo.state.Self

class ReducerImpl<S : Self, C : Contract>(
    private val component: KoloComponent<out ResultAction, S, C>,
    private val context: ReduceContext,
) : Reducer<S, C> {
    override fun reduce(
        self: S,
        contract: C,
        action: Action,
    ): S = component.process(context, self, contract, action)
}

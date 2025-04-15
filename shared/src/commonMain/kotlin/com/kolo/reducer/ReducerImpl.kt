package com.kolo.reducer

import com.kolo.action.Action
import com.kolo.action.ResultAction
import com.kolo.component.common.KoloComponent
import com.kolo.component.composition.context.reduce.ReduceContext
import com.kolo.state.Contract
import com.kolo.state.Self

class ReducerImpl<S : Self>(
    private val component: KoloComponent<out ResultAction, S, out Contract>,
    private val context: ReduceContext,
) : Reducer<S> {
    override fun reduce(
        self: S,
        action: Action,
    ): S = component.process(context, self, action)
}

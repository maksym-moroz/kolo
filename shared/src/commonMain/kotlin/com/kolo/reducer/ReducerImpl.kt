package com.kolo.reducer

import com.kolo.action.Action
import com.kolo.component.common.KoloComponent
import com.kolo.component.composition.context.reduce.ReduceContext
import com.kolo.state.State

class ReducerImpl<S : State>(
    private val component: KoloComponent<*, S>,
    private val context: ReduceContext,
) : Reducer<S> {
    override fun reduce(
        state: S,
        action: Action,
    ): S = component.process(context, state, action)
}

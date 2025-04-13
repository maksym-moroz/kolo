package com.kolo.example.component

import com.kolo.action.Action
import com.kolo.action.ResultAction.WithNoResultAction
import com.kolo.component.common.KoloComponent
import com.kolo.component.composition.container.EffectContainer
import com.kolo.component.composition.content.UiContent
import com.kolo.component.composition.context.reduce.ReduceContext
import com.kolo.example.action.RootAction
import com.kolo.example.state.RootState

class RootComponent(
    override val content: UiContent<RootState>,
    initialState: RootState,
    container: EffectContainer,
) : KoloComponent<WithNoResultAction, RootState>(container, initialState) {
    override fun ReduceContext.reduce(
        state: RootState,
        action: Action,
    ): RootState =
        when (action) {
            RootAction.Decrement -> state.copy(counter = state.counter - 1)
            RootAction.Increment -> state.copy(counter = state.counter + 1)
            RootAction.Reset -> state.copy(counter = 0)
            else -> state
        }
}

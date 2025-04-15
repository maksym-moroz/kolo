package com.kolo.example.component

import com.kolo.action.Action
import com.kolo.action.ResultAction.WithNoResultAction
import com.kolo.component.common.KoloComponent
import com.kolo.component.composition.container.EffectContainer
import com.kolo.component.composition.content.UiContent
import com.kolo.component.composition.context.reduce.ReduceContext
import com.kolo.example.action.RootAction
import com.kolo.example.state.RootSelf
import com.kolo.state.WithNoContract

class RootComponent(
    override val content: UiContent<RootSelf>,
    container: EffectContainer,
    initialSelf: RootSelf,
) : KoloComponent<WithNoResultAction, RootSelf, WithNoContract>(container, initialSelf) {
    override fun ReduceContext.reduce(
        self: RootSelf,
        contract: WithNoContract,
        action: Action,
    ): RootSelf =
        when (action) {
            RootAction.Decrement -> self.copy(counter = self.counter - 1)
            RootAction.Increment -> self.copy(counter = self.counter + 1)
            RootAction.Reset -> self.copy(counter = 0)
            else -> self
        }
}

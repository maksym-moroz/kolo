package com.kolo.example.b.component

import com.kolo.action.Action
import com.kolo.action.ResultAction.WithNoResultAction
import com.kolo.component.common.KoloComponent
import com.kolo.component.composition.container.EffectContainer
import com.kolo.component.composition.content.UiContent
import com.kolo.component.composition.context.reduce.ReduceContext
import com.kolo.example.b.action.ActionB
import com.kolo.example.b.state.SelfB
import com.kolo.state.WithNoContract

class ComponentB(
    override val content: UiContent<SelfB>,
    container: EffectContainer,
    initialSelf: SelfB,
) : KoloComponent<WithNoResultAction, SelfB, WithNoContract>(container, initialSelf) {
    override fun ReduceContext.reduce(
        self: SelfB,
        contract: WithNoContract,
        action: Action,
    ): SelfB =
        when (action) {
            ActionB.Decrement -> self.copy(counter = self.counter - 1)
            ActionB.Increment -> self.copy(counter = self.counter + 1)
            ActionB.Reset -> self.copy(counter = 0)
            else -> self
        }
}

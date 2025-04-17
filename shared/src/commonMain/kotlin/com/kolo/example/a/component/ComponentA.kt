package com.kolo.example.a.component

import com.kolo.action.Action
import com.kolo.action.ResultAction.WithNoResultAction
import com.kolo.component.common.KoloComponent
import com.kolo.component.composition.container.EffectContainer
import com.kolo.component.composition.content.UiContent
import com.kolo.component.composition.context.reduce.ReduceContext
import com.kolo.example.a.action.ActionA
import com.kolo.example.a.state.SelfA
import com.kolo.state.WithNoContract

class ComponentA(
    override val content: UiContent<SelfA>,
    container: EffectContainer,
    initialSelf: SelfA,
) : KoloComponent<WithNoResultAction, SelfA, WithNoContract>(container, initialSelf) {
    override fun ReduceContext.reduce(
        self: SelfA,
        contract: WithNoContract,
        action: Action,
    ): SelfA =
        when (action) {
            ActionA.Decrement -> self.copy(counter = self.counter - 1)
            ActionA.Increment -> self.copy(counter = self.counter + 1)
            ActionA.Reset -> self.copy(counter = 0)
            else -> self
        }
}

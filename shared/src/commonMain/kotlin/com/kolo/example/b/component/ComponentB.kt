package com.kolo.example.b.component

import com.kolo.action.Action
import com.kolo.action.ResultAction.WithNoResultAction
import com.kolo.component.common.KoloComponent
import com.kolo.component.composition.container.EffectContainer
import com.kolo.component.composition.content.UiContent
import com.kolo.component.composition.context.reduce.ReduceContext
import com.kolo.example.b.action.ActionB
import com.kolo.example.b.state.contract.ContractB
import com.kolo.example.b.state.self.SelfB

class ComponentB(
    override val content: UiContent<SelfB>,
    container: EffectContainer,
    initialSelf: SelfB,
) : KoloComponent<WithNoResultAction, SelfB, ContractB>(container, initialSelf) {
    override fun ReduceContext.reduce(
        self: SelfB,
        contract: ContractB,
        action: Action,
    ): SelfB =
        when (action) {
            ActionB.Decrement -> self.copy(accumulator = self.accumulator - 10)
            ActionB.Increment -> self.copy(accumulator = self.accumulator + 10)
            ActionB.Reset -> self.copy(accumulator = 0)
            else -> self
        }
}

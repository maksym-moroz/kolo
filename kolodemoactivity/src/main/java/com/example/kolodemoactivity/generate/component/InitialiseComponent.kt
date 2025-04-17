package com.example.kolodemoactivity.generate.component

import androidx.compose.runtime.Composable
import com.example.kolodemoactivity.example.content.UiContentA
import com.example.kolodemoactivity.example.content.UiContentB
import com.kolo.component.composition.container.EffectContainer
import com.kolo.component.composition.content.UiContent
import com.kolo.component.composition.context.store.StoreContext
import com.kolo.example.a.component.ComponentA
import com.kolo.example.a.state.SelfA
import com.kolo.example.b.component.ComponentB
import com.kolo.example.b.state.self.SelfB
import com.kolo.state.Contract
import kotlinx.coroutines.flow.StateFlow

fun initialiseComponentA(
    state: SelfA,
    container: EffectContainer,
): ComponentA {
    val content =
        object : UiContent<SelfA>() {
            @Composable
            override fun android(
                storeContext: StoreContext,
                state: SelfA,
                contract: Contract,
            ) {
                UiContentA(storeContext, state)
            }

            override fun ios(
                storeContext: StoreContext,
                state: StateFlow<SelfA>,
            ) = Unit
        }

    return ComponentA(content, container, state)
}

fun initialiseComponentB(
    state: SelfB,
    container: EffectContainer,
): ComponentB {
    val content =
        object : UiContent<SelfB>() {
            @Composable
            override fun android(
                storeContext: StoreContext,
                state: SelfB,
                contract: Contract,
            ) {
                UiContentB(storeContext, state)
            }

            override fun ios(
                storeContext: StoreContext,
                state: StateFlow<SelfB>,
            ) = Unit
        }

    return ComponentB(content, container, state)
}

package com.example.kolodemoactivity.generate.component

import androidx.compose.runtime.Composable
import com.example.kolodemoactivity.example.content.ExampleUiContent
import com.kolo.component.composition.container.EffectContainer
import com.kolo.component.composition.content.UiContent
import com.kolo.component.composition.context.store.StoreContext
import com.kolo.example.a.component.ComponentA
import com.kolo.example.a.state.SelfA
import com.kolo.state.Contract
import kotlinx.coroutines.flow.StateFlow

fun initialiseComponent(
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
                ExampleUiContent(storeContext, state)
            }

            override fun ios(
                storeContext: StoreContext,
                state: StateFlow<SelfA>,
            ) = Unit
        }

    return ComponentA(content, state, container)
}

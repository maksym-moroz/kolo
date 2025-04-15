package com.example.kolodemoactivity.generate.component

import androidx.compose.runtime.Composable
import com.example.kolodemoactivity.example.content.ExampleUiContent
import com.kolo.component.composition.container.EffectContainer
import com.kolo.component.composition.content.UiContent
import com.kolo.component.composition.context.store.StoreContext
import com.kolo.example.component.RootComponent
import com.kolo.example.state.RootSelf
import kotlinx.coroutines.flow.StateFlow

fun initialiseComponent(
    state: RootSelf,
    container: EffectContainer,
): RootComponent {
    val content =
        object : UiContent<RootSelf>() {
            @Composable
            override fun android(
                storeContext: StoreContext,
                state: RootSelf,
            ) {
                ExampleUiContent(storeContext, state)
            }

            override fun ios(
                storeContext: StoreContext,
                state: StateFlow<RootSelf>,
            ) = Unit
        }

    return RootComponent(content, state, container)
}

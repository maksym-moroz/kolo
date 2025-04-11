package com.kolo.component.composition.content

import androidx.compose.runtime.Composable
import com.kolo.component.composition.context.store.StoreContext
import com.kolo.state.State

expect abstract class UiContent<S : State> {
    @Composable
    abstract fun android(
        storeContext: StoreContext,
        state: S,
    )

    abstract fun ios(
        storeContext: StoreContext,
        state: S,
    ): Any
}

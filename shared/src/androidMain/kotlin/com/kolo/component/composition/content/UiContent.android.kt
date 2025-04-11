package com.kolo.component.composition.content

import androidx.compose.runtime.Composable
import com.kolo.component.composition.context.store.StoreContext
import com.kolo.state.State

actual abstract class UiContent<S : State> {
    // context(StoreContext)
    @Composable
    actual abstract fun android(
        storeContext: StoreContext,
        state: S,
    )

    actual abstract fun ios(
        storeContext: StoreContext,
        state: S,
    ): Any
}

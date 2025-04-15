package com.kolo.component.composition.content

import androidx.compose.runtime.Composable
import com.kolo.component.composition.context.store.StoreContext
import com.kolo.state.Self
import kotlinx.coroutines.flow.StateFlow

actual abstract class UiContent<S : Self> {
    @Composable
    actual abstract fun android(
        storeContext: StoreContext,
        state: S,
    )

    actual abstract fun ios(
        storeContext: StoreContext,
        state: StateFlow<S>,
    ): Any
}

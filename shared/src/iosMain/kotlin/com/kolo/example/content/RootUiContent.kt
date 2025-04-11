package com.kolo.example.content

import com.kolo.component.composition.content.UiContent
import com.kolo.component.composition.context.store.StoreContext
import com.kolo.example.state.RootState

abstract class RootUiContent : UiContent<RootState>() {
    abstract fun content(
        storeContext: StoreContext,
        state: RootState,
    )
}

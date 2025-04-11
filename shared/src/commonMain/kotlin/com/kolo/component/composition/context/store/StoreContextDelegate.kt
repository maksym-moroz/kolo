package com.kolo.component.composition.context.store

import com.kolo.action.Action
import com.kolo.state.State
import com.kolo.store.Store

class StoreContextDelegate<S : State>(
    private val store: Store<S>,
) : StoreContext {
    override /*suspend*/ fun dispatch(action: Action) {
        store.dispatch(action)
    }
}

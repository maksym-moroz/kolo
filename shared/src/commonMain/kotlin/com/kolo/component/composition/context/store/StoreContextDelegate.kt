package com.kolo.component.composition.context.store

import com.kolo.action.Action
import com.kolo.state.Contract
import com.kolo.state.Self
import com.kolo.store.Store

class StoreContextDelegate<S : Self, C : Contract>(
    private val store: Store<S, C>,
) : StoreContext {
    override /*suspend*/ fun dispatch(action: Action) {
        store.dispatch(action)
    }
}

package com.kolo.middleware.communication

import com.kolo.action.Action
import com.kolo.component.composition.context.store.StoreContext

class ParentDispatchImpl(
    private val storeContext: StoreContext,
) : ParentDispatch {
    override fun dispatch(action: Action) {
        storeContext.dispatch(action)
    }
}

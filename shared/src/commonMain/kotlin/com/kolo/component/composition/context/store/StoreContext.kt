package com.kolo.component.composition.context.store

import com.kolo.action.Action

interface StoreContext {
    // suspend
    fun dispatch(action: Action)
}

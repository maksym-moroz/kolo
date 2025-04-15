package com.kolo.component.extensions

import com.kolo.action.ResultAction
import com.kolo.component.common.KoloComponent
import com.kolo.example.component.RootComponent
import com.kolo.example.state.RootSelf

// file for workarounds, hacks and moving forward
// one can hope it won't grow

fun RootComponent.asGenericComponent(): KoloComponent<ResultAction, RootSelf> {
    @Suppress("UNCHECKED_CAST")
    return this as KoloComponent<ResultAction, RootSelf>
}

package com.kolo.component.extensions

import com.kolo.action.ResultAction
import com.kolo.component.common.KoloComponent
import com.kolo.example.component.RootComponent
import com.kolo.example.state.RootState

// file for workarounds, hacks and moving forward
// one can hope it won't grow

fun RootComponent.asGenericComponent(): KoloComponent<ResultAction, RootState> {
    @Suppress("UNCHECKED_CAST")
    return this as KoloComponent<ResultAction, RootState>
}

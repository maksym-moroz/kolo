package com.kolo.component.extensions

import com.kolo.action.ResultAction
import com.kolo.component.common.KoloComponent
import com.kolo.example.a.component.ComponentA
import com.kolo.example.a.state.SelfA

// file for workarounds, hacks and moving forward
// one can hope it won't grow

fun ComponentA.asGenericComponent(): KoloComponent<ResultAction, SelfA> {
    @Suppress("UNCHECKED_CAST")
    return this as KoloComponent<ResultAction, SelfA>
}

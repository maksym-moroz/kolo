package com.kolo.store.configuration

import com.kolo.effect.Effect
import com.kolo.middleware.communication.ParentDispatch

data class StoreConfiguration(
    val actionEffects: List<Effect>,
    val eventEffects: List<Effect>,
    val parentDispatch: ParentDispatch,
)

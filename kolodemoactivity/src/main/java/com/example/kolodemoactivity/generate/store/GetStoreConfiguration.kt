package com.example.kolodemoactivity.generate.store

import com.kolo.effect.Effect
import com.kolo.middleware.communication.ParentDispatch
import com.kolo.store.configuration.StoreConfiguration

fun getStoreConfiguration(
    effects: List<Effect>,
    parentDispatch: ParentDispatch,
): StoreConfiguration {
    val storeConfiguration =
        StoreConfiguration(
            actionEffects = effects,
            eventEffects = effects,
            parentDispatch = parentDispatch,
        )
    return storeConfiguration
}

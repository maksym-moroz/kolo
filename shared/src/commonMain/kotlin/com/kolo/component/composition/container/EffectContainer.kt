package com.kolo.component.composition.container

import com.kolo.effect.Effect

fun interface EffectContainer {
    fun effects(): List<Effect>
}

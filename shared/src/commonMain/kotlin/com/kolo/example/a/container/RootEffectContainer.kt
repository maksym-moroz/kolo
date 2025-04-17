package com.kolo.example.a.container

import com.kolo.component.composition.container.EffectContainer
import com.kolo.effect.Effect
import com.kolo.example.a.effect.ResetOnDecrementEffect

class RootEffectContainer : EffectContainer {
    override fun effects(): List<Effect> = listOf(ResetOnDecrementEffect())
}

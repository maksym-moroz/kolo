package com.kolo.example.b.container

import com.kolo.component.composition.container.EffectContainer
import com.kolo.effect.Effect
import com.kolo.example.a.effect.ResetOnDecrementEffect

class EffectContainerB : EffectContainer {
    override fun effects(): List<Effect> = listOf(ResetOnDecrementEffect())
}

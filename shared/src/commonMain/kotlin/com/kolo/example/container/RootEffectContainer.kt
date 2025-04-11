package com.kolo.example.container

import com.kolo.component.composition.container.EffectContainer
import com.kolo.effect.Effect

class RootEffectContainer : EffectContainer {
    override fun effects() = emptyList<Effect>()
}

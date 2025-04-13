package com.example.kolodemoactivity.generate.component.configuration

import com.kolo.component.common.KoloComponent
import com.kolo.component.configuration.ComponentConfiguration

fun getComponentConfiguration(component: KoloComponent<*, *>): ComponentConfiguration {
    // todo(maksym) analyze component to determine flags

    return ComponentConfiguration(
        hasContracts = false,
        hasResults = false,
        hasEffects = true,
        hasEvents = true,
    )
}

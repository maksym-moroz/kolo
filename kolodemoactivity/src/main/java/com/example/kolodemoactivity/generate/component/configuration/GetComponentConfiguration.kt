package com.example.kolodemoactivity.generate.component.configuration

import com.kolo.component.common.KoloComponent
import com.kolo.component.configuration.ComponentConfiguration
import com.kolo.example.a.component.ComponentA

fun getComponentConfiguration(component: KoloComponent<*, *, *>): ComponentConfiguration {
    // todo(maksym) analyze component to determine flags

    val configuration =
        if (component is ComponentA) {
            ComponentConfiguration(
                hasContracts = false,
                hasResults = false,
                hasEffects = true,
                hasEvents = true,
            )
        } else {
            ComponentConfiguration(
                hasContracts = true,
                hasResults = true,
                hasEffects = true,
                hasEvents = true,
            )
        }
    return configuration
}

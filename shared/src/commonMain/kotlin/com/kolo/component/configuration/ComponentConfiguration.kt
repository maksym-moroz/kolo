package com.kolo.component.configuration

// can be used to delegate part of initial parsing later

data class ComponentConfiguration(
    val hasContracts: Boolean,
    val hasResults: Boolean,
    val hasEffects: Boolean,
    val hasEvents: Boolean,
)

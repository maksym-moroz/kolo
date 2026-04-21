package com.focus.kolo

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface KoloRoute : NavKey {
    @Serializable
    data object Home : KoloRoute

    @Serializable
    data class Capability(
        val capabilityWireName: String
    ) : KoloRoute
}

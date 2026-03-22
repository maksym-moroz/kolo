package com.focus.kolo.debugmenu

import androidx.compose.runtime.Composable
import com.focus.kolo.config.AppConfigSourceLayer
import com.focus.kolo.config.AppConfigSourceState

@Composable
internal fun ConfigSourceSection(sourceState: AppConfigSourceState) {
    DebugMenuSection(title = "Config Sources") {
        ReadOnlyRow(
            label = "Active layers",
            value = sourceState.activeLayers
                .joinToString(" -> ", transform = ::formatLayer)
        )
        ReadOnlyRow(
            label = "Remote overrides active",
            value = sourceState.hasRemoteOverrides
                .toString()
        )
        ReadOnlyRow(
            label = "Local overrides active",
            value = sourceState.hasLocalOverrides
                .toString()
        )
    }
}

private fun formatLayer(layer: AppConfigSourceLayer): String = when (layer) {
    AppConfigSourceLayer.BUNDLED -> "Bundled"
    AppConfigSourceLayer.REMOTE -> "Remote"
    AppConfigSourceLayer.LOCAL -> "Local"
}

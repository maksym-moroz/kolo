package com.focus.kolo.config

data class AppConfigSourceState(
    val hasRemoteOverrides: Boolean = false,
    val hasLocalOverrides: Boolean = false
) {
    val activeLayers: List<AppConfigSourceLayer>
        get() =
            buildList {
                add(AppConfigSourceLayer.BUNDLED)

                if (hasRemoteOverrides) {
                    add(AppConfigSourceLayer.REMOTE)
                }
                if (hasLocalOverrides) {
                    add(AppConfigSourceLayer.LOCAL)
                }
            }
}

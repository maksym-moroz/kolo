package com.focus.kolo.debugmenu

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.window.ComposeUIViewController
import com.focus.kolo.createIosAppGraph
import platform.UIKit.UIViewController

class IosDebugMenuControllerFactory {
    private val appGraph by lazy {
        createIosAppGraph()
    }
    private val dependencies by lazy {
        object : DebugMenuDependencies {
            override val storeFactory = appGraph.storeFactory
            override val appConfigRepository = appGraph.appConfigRepository
            override val updateAppConfigOverrideUseCase = appGraph.updateAppConfigOverrideUseCase
            override val clearAppConfigOverrideUseCase = appGraph.clearAppConfigOverrideUseCase
            override val resetAppConfigOverridesUseCase = appGraph.resetAppConfigOverridesUseCase
        }
    }

    fun makeViewController(
        onClose: () -> Unit,
        onRestartRequired: () -> Unit
    ): UIViewController = ComposeUIViewController {
        MaterialTheme {
            DebugMenuRoute(
                dependencies = dependencies,
                onClose = onClose,
                onRestartRequired = onRestartRequired
            )
        }
    }
}

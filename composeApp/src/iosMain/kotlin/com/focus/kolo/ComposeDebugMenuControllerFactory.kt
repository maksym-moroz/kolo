package com.focus.kolo

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.window.ComposeUIViewController
import com.focus.kolo.debugmenu.DebugMenuHost
import platform.UIKit.UIViewController

class ComposeDebugMenuControllerFactory {
    private val appGraph by lazy {
        createIosAppGraph()
    }

    fun makeViewController(
        onClose: () -> Unit,
        onRestartRequired: () -> Unit
    ): UIViewController = ComposeUIViewController {
        MaterialTheme {
            DebugMenuHost(
                appGraph = appGraph,
                onClose = onClose,
                onRestartRequired = onRestartRequired
            )
        }
    }
}

package com.focus.kolo

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

class ComposeAppControllerFactory {
    fun makeViewController(): UIViewController = ComposeUIViewController {
        KoloApp()
    }
}

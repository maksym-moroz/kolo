package com.focus.kolo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.focus.kolo.appshell.ReferenceAppShell

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super
            .onCreate(savedInstanceState)

        setContent {
            KoloApp(appShellDefinition = ReferenceAppShell.definition)
        }
    }
}

package com.focus.kolo.debugmenu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import com.focus.kolo.KoloApplication
import com.focus.kolo.appshell.AppCapability
import com.jakewharton.processphoenix.ProcessPhoenix

class DebugMenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val app = application as KoloApplication
        if (
            !app.appShellDefinition
                .supports(AppCapability.DebugMenu)
        ) {
            finish()
            return
        }

        setContent {
            MaterialTheme {
                DebugMenuHost(
                    appGraph = app.appGraph,
                    onClose = ::finish,
                    onRestartRequired = { ProcessPhoenix.triggerRebirth(this) }
                )
            }
        }
    }
}

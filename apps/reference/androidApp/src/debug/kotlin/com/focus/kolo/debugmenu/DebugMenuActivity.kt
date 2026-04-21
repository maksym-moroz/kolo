package com.focus.kolo.debugmenu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import com.focus.kolo.KoloApplication
import com.jakewharton.processphoenix.ProcessPhoenix

class DebugMenuActivity : ComponentActivity() {
    private val dependencies: DebugMenuDependencies by lazy {
        val appGraph = (application as KoloApplication).appGraph

        object : DebugMenuDependencies {
            override val storeFactory = appGraph.storeFactory
            override val appConfigRepository = appGraph.appConfigRepository
            override val updateAppConfigOverrideUseCase = appGraph.updateAppConfigOverrideUseCase
            override val clearAppConfigOverrideUseCase = appGraph.clearAppConfigOverrideUseCase
            override val resetAppConfigOverridesUseCase = appGraph.resetAppConfigOverridesUseCase
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                DebugMenuRoute(
                    dependencies = dependencies,
                    onClose = ::finish,
                    onRestartRequired = { ProcessPhoenix.triggerRebirth(this) }
                )
            }
        }
    }
}

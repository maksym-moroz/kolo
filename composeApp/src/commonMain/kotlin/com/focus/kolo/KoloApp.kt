package com.focus.kolo

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.focus.kolo.appshell.AppShellDefinition
import com.focus.kolo.appshell.ReferenceAppShell

@Composable
fun KoloApp(appShellDefinition: AppShellDefinition = ReferenceAppShell.definition) {
    MaterialTheme {
        KoloNavigationHost(appShellDefinition = appShellDefinition)
    }
}

package com.focus.kolo

import android.app.Application
import com.focus.kolo.appshell.AppShellDefinition
import com.focus.kolo.appshell.ReferenceAppShell

class KoloApplication : Application() {
    val appGraph: AndroidAppGraph by lazy { createAndroidAppGraph(applicationContext) }
    val appShellDefinition: AppShellDefinition = ReferenceAppShell.definition
}

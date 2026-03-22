package com.focus.kolo

import android.app.Application

class KoloApplication : Application() {
    val appGraph: AndroidAppGraph by lazy { createAndroidAppGraph(applicationContext) }
}

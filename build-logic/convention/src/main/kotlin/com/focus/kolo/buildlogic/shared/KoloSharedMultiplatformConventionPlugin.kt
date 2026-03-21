package com.focus.kolo.buildlogic.shared

import org.gradle.api.Plugin
import org.gradle.api.Project

public class KoloSharedMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("kolo.android.kotlin.multiplatform.library")
            pluginManager.apply("dev.zacsweers.metro")
        }
    }
}

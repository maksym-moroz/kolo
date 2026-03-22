package com.focus.kolo.buildlogic.compose

import org.gradle.api.Plugin
import org.gradle.api.Project

public class KoloComposeMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager
                .apply("kolo.android.kotlin.multiplatform.library")
            pluginManager
                .apply("org.jetbrains.compose")
            pluginManager
                .apply("org.jetbrains.kotlin.plugin.compose")
        }
    }
}

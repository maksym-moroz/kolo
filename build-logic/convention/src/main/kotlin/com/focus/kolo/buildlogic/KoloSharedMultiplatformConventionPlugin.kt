package com.focus.kolo.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

public class KoloSharedMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.multiplatform")
            pluginManager.apply("com.android.kotlin.multiplatform.library")
            pluginManager.apply("dev.zacsweers.metro")

            extensions.configure<KotlinProjectExtension> {
                jvmToolchain(versionInt("java-toolchain"))
            }
        }
    }
}

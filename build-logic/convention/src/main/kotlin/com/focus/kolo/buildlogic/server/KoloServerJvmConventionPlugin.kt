package com.focus.kolo.buildlogic.server

import com.focus.kolo.buildlogic.convention.internal.configureDetekt
import com.focus.kolo.buildlogic.convention.internal.registerModuleQualityCheckTask
import com.focus.kolo.buildlogic.convention.internal.versionInt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

public class KoloServerJvmConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager
                .apply("org.jetbrains.kotlin.jvm")
            pluginManager
                .apply("io.ktor.plugin")
            pluginManager
                .apply("application")

            extensions.configure<KotlinJvmProjectExtension> {
                jvmToolchain(versionInt("java-toolchain"))
            }

            val qualityCheck = registerModuleQualityCheckTask()
            configureDetekt(qualityCheck)
            pluginManager
                .apply("com.autonomousapps.dependency-analysis")
        }
    }
}

package com.focus.kolo.buildlogic.android

import com.android.build.api.dsl.LibraryExtension
import com.focus.kolo.buildlogic.convention.internal.configureDetekt
import com.focus.kolo.buildlogic.convention.internal.registerModuleQualityCheckTask
import com.focus.kolo.buildlogic.convention.internal.versionInt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

public class KoloAndroidComposeLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager
                .apply("com.android.library")
            pluginManager
                .apply("org.jetbrains.compose")
            pluginManager
                .apply("org.jetbrains.kotlin.plugin.compose")

            extensions.configure<KotlinProjectExtension> {
                jvmToolchain(versionInt("java-toolchain"))
            }

            val qualityCheck = registerModuleQualityCheckTask()
            configureDetekt(qualityCheck)
            pluginManager
                .apply("com.autonomousapps.dependency-analysis")

            qualityCheck.configure {
                dependsOn(
                    tasks
                        .named("lintDebug")
                )
            }

            extensions.configure<LibraryExtension> {
                compileSdk = versionInt("android-compileSdk")

                defaultConfig {
                    minSdk = versionInt("android-minSdk")
                }

                buildFeatures {
                    compose = true
                }

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }
            }
        }
    }
}

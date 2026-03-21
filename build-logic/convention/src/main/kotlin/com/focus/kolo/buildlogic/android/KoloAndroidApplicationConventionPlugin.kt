package com.focus.kolo.buildlogic.android

import com.android.build.api.dsl.ApplicationExtension
import com.focus.kolo.buildlogic.convention.internal.configureDetekt
import com.focus.kolo.buildlogic.convention.internal.registerModuleQualityCheckTask
import com.focus.kolo.buildlogic.convention.internal.versionInt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

public class KoloAndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")
            pluginManager.apply("org.jetbrains.compose")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            extensions.configure<KotlinProjectExtension> {
                jvmToolchain(versionInt("java-toolchain"))
            }

            val qualityCheck = registerModuleQualityCheckTask()
            configureDetekt(qualityCheck)
            pluginManager.apply("com.autonomousapps.dependency-analysis")

            qualityCheck.configure {
                dependsOn(tasks.named("lintDebug"))
            }

            extensions.configure<ApplicationExtension> {
                compileSdk = versionInt("android-compileSdk")

                defaultConfig {
                    minSdk = versionInt("android-minSdk")
                    targetSdk = versionInt("android-targetSdk")
                }

                buildFeatures {
                    compose = true
                }

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }

                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = false
                    }
                }
            }
        }
    }
}

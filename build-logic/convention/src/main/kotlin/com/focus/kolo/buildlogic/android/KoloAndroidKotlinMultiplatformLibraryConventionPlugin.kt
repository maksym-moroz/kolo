package com.focus.kolo.buildlogic.android

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.focus.kolo.buildlogic.convention.internal.configureDetekt
import com.focus.kolo.buildlogic.convention.internal.registerModuleQualityCheckTask
import com.focus.kolo.buildlogic.convention.internal.versionInt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

public class KoloAndroidKotlinMultiplatformLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.multiplatform")
            pluginManager.apply("com.android.kotlin.multiplatform.library")

            extensions.configure<KotlinProjectExtension> {
                jvmToolchain(versionInt("java-toolchain"))
            }

            val kotlin = extensions.getByType<KotlinProjectExtension>()
            (kotlin as ExtensionAware).extensions.configure<KotlinMultiplatformAndroidLibraryTarget>("android") {
                compileSdk = versionInt("android-compileSdk")
                minSdk = versionInt("android-minSdk")
            }

            val qualityCheck = registerModuleQualityCheckTask()
            configureDetekt(qualityCheck)

            tasks.matching { it.name == "lintAnalyzeAndroidHostTest" }.configureEach {
                qualityCheck.get().dependsOn(this)
            }
        }
    }
}

package com.focus.kolo.buildlogic.convention.internal

import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register

private const val MODULE_QUALITY_CHECK_TASK = "qualityCheck"

internal fun Project.registerModuleQualityCheckTask(): TaskProvider<*> = tasks.register(MODULE_QUALITY_CHECK_TASK) {
    group = "verification"
    description = "Runs module quality checks."
}

internal fun Project.configureDetekt(qualityCheck: TaskProvider<*>) {
    pluginManager
        .apply("dev.detekt")

    extensions.configure<DetektExtension> {
        buildUponDefaultConfig
            .set(true)
        allRules
            .set(false)
        parallel
            .set(true)
        basePath
            .set(rootProject.layout.projectDirectory)
        config
            .setFrom(
                rootProject
                    .files("config/detekt/detekt.yml")
            )
    }

    tasks.withType(Detekt::class.java).configureEach {
        jvmTarget
            .set(version("java-toolchain"))
        source = fileTree(projectDir) {
            include("src/**/*.kt")
            exclude("**/build/**")
        }
        reports {
            html.required
                .set(true)
            sarif.required
                .set(true)
        }
    }

    tasks.named("check").configure {
        dependsOn(
            tasks
                .named("detekt")
        )
    }

    qualityCheck.configure {
        dependsOn(
            tasks
                .named("detekt")
        )
    }
}

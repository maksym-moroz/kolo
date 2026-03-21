package com.focus.kolo.buildlogic.root

import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

private const val STORE_API_CHECK_TASK = ":shared:core:store:api:apiCheck"

public class KoloRootConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val extension = extensions.create<KoloRootExtension>("kolo")

            val qualityCheck =
                tasks.register("qualityCheck") {
                    group = "verification"
                    description = "Runs repository quality checks."
                    dependsOn(STORE_API_CHECK_TASK)
                    dependsOn(
                        extension.qualityModules.map { modules ->
                            modules.map { "$it:qualityCheck" }
                        },
                    )
                }

            val qualityFix =
                tasks.register("qualityFix") {
                    group = "formatting"
                    description = "Applies repository formatting rules."
                }

            tasks.register("dependencyHealth") {
                group = "verification"
                description = "Generates dependency analysis reports for supported modules."
                dependsOn(
                    extension.dependencyHealthModules.map { modules ->
                        modules.map { "$it:projectHealth" }
                    },
                )
            }

            pluginManager.withPlugin("com.diffplug.spotless") {
                extensions.configure<SpotlessExtension> {
                    kotlin {
                        target("**/*.kt")
                        targetExclude("**/build/**", "**/node_modules/**")
                        ktlint("1.8.0")
                    }
                    kotlinGradle {
                        target("**/*.gradle.kts")
                        targetExclude("**/build/**", "**/node_modules/**")
                        ktlint("1.8.0")
                    }
                    format("misc") {
                        target(".gitignore", ".gitattributes", "**/*.md", "**/*.yaml", "**/*.yml")
                        targetExclude("**/build/**", "**/node_modules/**")
                        trimTrailingWhitespace()
                        leadingTabsToSpaces()
                        endWithNewline()
                    }
                }

                qualityCheck.configure {
                    dependsOn(tasks.named("spotlessCheck"))
                }

                qualityFix.configure {
                    dependsOn(tasks.named("spotlessApply"))
                }
            }
        }
    }
}

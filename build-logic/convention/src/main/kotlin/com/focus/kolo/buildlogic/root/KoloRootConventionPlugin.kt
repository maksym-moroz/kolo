package com.focus.kolo.buildlogic.root

import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

private const val STORE_API_CHECK_TASK = ":shared:core:store:api:apiCheck"

private fun ktlintEditorConfigOverrides(): Map<String, String> = mapOf(
    "ktlint_code_style" to "android_studio",
    "max_line_length" to "140",
    "ij_kotlin_allow_trailing_comma" to "false",
    "ij_kotlin_allow_trailing_comma_on_call_site" to "false",
    "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
    "ktlint_function_signature_body_expression_wrapping" to "default",
    "ktlint_function_signature_rule_force_multiline_when_parameter_count_greater_or_equal_than" to "2",
    "ktlint_class_signature_rule_force_multiline_when_parameter_count_greater_or_equal_than" to "1",
    "ktlint_argument_list_wrapping_ignore_when_parameter_count_greater_or_equal_than" to "4",
    "ij_kotlin_line_break_after_multiline_when_entry" to "false",
    "ktlint_standard_chain-method-continuation" to "enabled",
    "ktlint_chain_method_rule_force_multiline_when_chain_operator_count_greater_or_equal_than" to "2",
    "ktlint_standard_no-blank-lines-in-chained-method-calls" to "disabled",
    "ktlint_standard_no-line-break-before-assignment" to "disabled",
    "ktlint_standard_multiline-expression-wrapping" to "disabled"
)

public class KoloRootConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val extension =
                extensions
                    .create<KoloRootExtension>("kolo")

            val qualityCheck =
                tasks.register("qualityCheck") {
                    group = "verification"
                    description = "Runs repository quality checks."
                    dependsOn(STORE_API_CHECK_TASK)
                    dependsOn(
                        extension.qualityModules.map { modules ->
                            modules
                                .map { "$it:qualityCheck" }
                        }
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
                        modules
                            .map { "$it:projectHealth" }
                    }
                )
            }

            pluginManager.withPlugin("com.diffplug.spotless") {
                extensions.configure<SpotlessExtension> {
                    kotlin {
                        target("**/*.kt")
                        targetExclude("**/build/**", "**/node_modules/**")
                        ktlint("1.8.0")
                            .editorConfigOverride(ktlintEditorConfigOverrides())
                    }
                    kotlinGradle {
                        target("**/*.gradle.kts")
                        targetExclude("**/build/**", "**/node_modules/**")
                        ktlint("1.8.0")
                            .editorConfigOverride(ktlintEditorConfigOverrides())
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
                    dependsOn(
                        tasks
                            .named("spotlessCheck")
                    )
                }

                qualityFix.configure {
                    dependsOn(
                        tasks
                            .named("spotlessApply")
                    )
                }
            }
        }
    }
}

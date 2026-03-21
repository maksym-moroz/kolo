import com.diffplug.gradle.spotless.SpotlessExtension
import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.dependencyAnalysis) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.ktor) apply false
    alias(libs.plugins.mappie) apply false
    alias(libs.plugins.metro) apply false

    alias(libs.plugins.binaryCompatibilityValidator) apply false
    alias(libs.plugins.spotless)
}

configure<SpotlessExtension> {
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

val qualityCheck =
    tasks.register("qualityCheck") {
        group = "verification"
        description = "Runs repository quality checks."
        dependsOn(
            "spotlessCheck",
            ":shared:core:store:api:apiCheck",
        )
    }

val qualityFix =
    tasks.register("qualityFix") {
        group = "formatting"
        description = "Applies repository formatting rules."
        dependsOn("spotlessApply")
    }

subprojects {
    fun configureDetektForProject() {
        pluginManager.apply("dev.detekt")

        configure<DetektExtension> {
            buildUponDefaultConfig = true
            allRules = false
            parallel = true
            basePath.set(rootProject.layout.projectDirectory)
        }

        tasks.withType(Detekt::class.java).configureEach {
            jvmTarget = "17"
            setSource(
                fileTree(projectDir) {
                    include("src/**/*.kt")
                    exclude("**/build/**")
                },
            )
            reports {
                html.required.set(true)
                sarif.required.set(true)
            }
        }

        tasks.named("check").configure {
            dependsOn(tasks.named("detekt"))
        }

        rootProject.tasks.named(qualityCheck.name).configure {
            dependsOn(tasks.named("detekt"))
        }
    }

    pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
        configureDetektForProject()
    }

    pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
        configureDetektForProject()
        pluginManager.apply("com.autonomousapps.dependency-analysis")
    }

    pluginManager.withPlugin("com.android.application") {
        configureDetektForProject()
        pluginManager.apply("com.autonomousapps.dependency-analysis")

        rootProject.tasks.named(qualityCheck.name).configure {
            dependsOn(tasks.named("lintDebug"))
        }
    }

    pluginManager.withPlugin("com.android.kotlin.multiplatform.library") {
        tasks.matching { it.name == "lintAnalyzeAndroidHostTest" }.configureEach {
            rootProject.tasks.named(qualityCheck.name).configure {
                dependsOn(this@configureEach)
            }
        }
    }
}

tasks.register("dependencyHealth") {
    group = "verification"
    description = "Generates dependency analysis reports for supported modules."
    dependsOn(
        ":androidApp:projectHealth",
        ":server:projectHealth",
    )
}

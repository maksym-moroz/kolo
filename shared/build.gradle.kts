import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.internal.config.LanguageFeature

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform) // todo find a way to remove
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.touchlabSkie)
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8)
                }
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        all {
            languageSettings {
                enableLanguageFeature(LanguageFeature.ExplicitBackingFields.name)
                enableLanguageFeature(LanguageFeature.WhenGuards.name)
                enableLanguageFeature(LanguageFeature.ContextReceivers.name)
                enableLanguageFeature(LanguageFeature.ExpectActualClasses.name)
            }
        }

        compilerOptions {
            freeCompilerArgs.add(
                // https://youtrack.jetbrains.com/issue/KT-73255
                // "-Xannotation-default-target=param-property",
                "-Xskip-prerelease-check",
            )
        }

        commonMain.dependencies {
            // put your multiplatform dependencies here
            implementation(libs.kotlinx.coroutines.core)
            implementation(compose.runtime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        androidMain.dependencies {
            implementation(libs.compose.runtime)
        }
        iosMain.dependencies {
        }
    }
}

android {
    namespace = "com.example.kolo"
    compileSdk = 35
    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

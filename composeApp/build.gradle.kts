import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kolo.compose.multiplatform)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    android {
        namespace = "com.focus.kolo.composeapp"
        withHostTest {}
        androidResources {
            enable = true
        }
    }

    iosArm64()
    iosSimulatorArm64()

    targets.withType(KotlinNativeTarget::class.java).configureEach {
        binaries.framework {
            baseName = "ComposeApp"
            isStatic = false
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.debugmenu)
            implementation(projects.shared)
            implementation(projects.shared.core.appshell)
            implementation(libs.compose.navigation3.ui)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.kotlinx.serialization.json)
        }
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
        }
    }
}

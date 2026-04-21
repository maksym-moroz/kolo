import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kolo.compose.multiplatform)
}

kotlin {
    android {
        namespace = "com.focus.kolo.debugmenu"
        withHostTest {}
    }

    iosArm64()
    iosSimulatorArm64()

    targets.withType(KotlinNativeTarget::class.java).configureEach {
        binaries.framework {
            baseName = "DebugMenu"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared)
            implementation(projects.shared.core.config.api)
            implementation(projects.shared.core.config.impl)
            implementation(projects.shared.core.store.api)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
        }
    }
}

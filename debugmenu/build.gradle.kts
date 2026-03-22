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
            api(projects.shared.core.config.api)
            api(projects.shared.core.config.impl)
            api(projects.shared.core.store.api)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
        }
        iosMain.dependencies {
            implementation(projects.shared)
        }
    }
}

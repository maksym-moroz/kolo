import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kolo.shared.multiplatform)
}

kotlin {
    android {
        namespace = "com.focus.kolo.shared"
    }

    iosArm64()
    iosSimulatorArm64()

    targets.withType(KotlinNativeTarget::class.java).configureEach {
        binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.core.config.api)
            implementation(projects.shared.core.config.impl)
            implementation(projects.shared.core.store.api)
            implementation(projects.shared.core.store.impl)
            implementation(libs.metro.runtime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

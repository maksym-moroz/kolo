import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kolo.shared.multiplatform)
}

kotlin {
    jvmToolchain(17)

    android {
        namespace = "com.focus.kolo.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    iosArm64()
    iosSimulatorArm64()

    targets.withType(KotlinNativeTarget::class.java).configureEach {
        binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.core.store.api)
            implementation(projects.shared.core.store.impl)
            implementation(libs.metro.runtime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

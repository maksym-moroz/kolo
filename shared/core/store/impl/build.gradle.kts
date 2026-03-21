plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.metro)
}

kotlin {
    jvmToolchain(17)

    android {
        namespace = "com.focus.kolo.shared.core.store.impl"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    iosArm64()
    iosSimulatorArm64()

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.core.store.api)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.metro.runtime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

plugins {
    alias(libs.plugins.kolo.android.kotlin.multiplatform.library)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.focus.kolo.shared.core.store.impl"
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

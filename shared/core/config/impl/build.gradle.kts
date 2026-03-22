plugins {
    alias(libs.plugins.kolo.android.kotlin.multiplatform.library)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.focus.kolo.shared.core.config.impl"
    }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.core.config.api)
            implementation(libs.datastore.preferences)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.metro.runtime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

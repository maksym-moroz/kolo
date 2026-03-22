plugins {
    alias(libs.plugins.kolo.android.kotlin.multiplatform.library)
}

kotlin {
    android {
        namespace = "com.focus.kolo.shared.core.config.api"
    }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.coroutines.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

plugins {
    alias(libs.plugins.kolo.android.kotlin.multiplatform.library)
    alias(libs.plugins.binaryCompatibilityValidator)
}

kotlin {
    android {
        namespace = "com.focus.kolo.shared.core.store.api"
    }

    iosArm64()
    iosSimulatorArm64()

    jvm()

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.coroutines.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

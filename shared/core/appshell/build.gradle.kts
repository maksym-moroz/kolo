plugins {
    alias(libs.plugins.kolo.android.kotlin.multiplatform.library)
}

kotlin {
    android {
        namespace = "com.focus.kolo.shared.core.appshell"
    }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

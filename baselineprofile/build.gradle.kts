plugins {
    alias(libs.plugins.androidTest)
    alias(libs.plugins.androidx.baselineprofile)
}

android {
    namespace = "com.focus.kolo.baselineprofile"
    compileSdk =
        libs.versions.android.compileSdk
            .get()
            .toInt()
    targetProjectPath = ":androidApp"

    defaultConfig {
        minSdk =
            libs.versions.android.minSdkBaselineProfile
                .get()
                .toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        managedDevices {
            localDevices {
                create("pixel6Api31") {
                    device = "Pixel 6"
                    apiLevel = 31
                    systemImageSource = "aosp"
                }
            }
        }
    }
}

baselineProfile {
    managedDevices += "pixel6Api31"
    useConnectedDevices = false
}

dependencies {
    implementation(libs.androidx.benchmark.macro.junit4)
    implementation(libs.androidx.testExt.junit)
    implementation(libs.androidx.test.uiautomator)
}

plugins {
    alias(libs.plugins.kolo.android.application)
}

android {
    namespace = "com.focus.kolo"

    defaultConfig {
        applicationId = "com.focus.kolo"
        versionCode =
            providers
                .gradleProperty("kolo.version.code")
                .get()
                .toInt()
        versionName =
            providers
                .gradleProperty("kolo.version.name")
                .get()
    }
}

dependencies {
    implementation(projects.composeApp)
    implementation(projects.shared)
    implementation(projects.shared.core.config.impl)

    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.profileinstaller)

    debugImplementation(projects.debugmenu)

    debugImplementation(libs.compose.material3)
    debugImplementation(libs.process.phoenix)

    baselineProfile(projects.baselineprofile)
}

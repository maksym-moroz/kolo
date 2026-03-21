plugins {
    alias(libs.plugins.kolo.android.application)
}

android {
    namespace = "com.focus.kolo"

    defaultConfig {
        applicationId = "com.focus.kolo"
        versionCode = providers.gradleProperty("kolo.version.code").get().toInt()
        versionName = providers.gradleProperty("kolo.version.name").get()
    }
}

dependencies {
    implementation(projects.composeApp)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.profileinstaller)

    baselineProfile(projects.baselineprofile)
}

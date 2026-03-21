plugins {
    alias(libs.plugins.kolo.android.application)
}

val appVersionCode = providers.gradleProperty("kolo.version.code").get().toInt()
val appVersionName = providers.gradleProperty("kolo.version.name").get()

android {
    namespace = "com.focus.kolo"

    defaultConfig {
        applicationId = "com.focus.kolo"
        versionCode = appVersionCode
        versionName = appVersionName
    }
}

dependencies {
    implementation(projects.composeApp)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.runtime)
}

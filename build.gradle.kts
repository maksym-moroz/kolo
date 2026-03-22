plugins {
    alias(libs.plugins.kolo.root)
    alias(libs.plugins.spotless)
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
    alias(libs.plugins.androidTest) apply false
    alias(libs.plugins.androidx.baselineprofile) apply false
    alias(libs.plugins.binaryCompatibilityValidator) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.dependencyAnalysis) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.ktor) apply false
    alias(libs.plugins.mappie) apply false
    alias(libs.plugins.metro) apply false
}

kolo {
    qualityModules.addAll(
        listOf(
            ":androidApp",
            ":composeApp",
            ":debugmenu",
            ":server",
            ":shared",
            ":shared:core:config:api",
            ":shared:core:config:impl",
            ":shared:core:store:api",
            ":shared:core:store:impl"
        )
    )
    dependencyHealthModules.addAll(
        listOf(
            ":androidApp",
            ":server"
        )
    )
}

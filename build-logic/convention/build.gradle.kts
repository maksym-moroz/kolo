plugins {
    `kotlin-dsl`
}

group = "com.focus.kolo.buildlogic"

kotlin {
    jvmToolchain(
        libs.versions.java.toolchain
            .get()
            .toInt(),
    )
    explicitApi()
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.spotless.gradlePlugin)
}

tasks.validatePlugins {
    enableStricterValidation.set(true)
}

gradlePlugin {
    plugins {
        register("koloAndroidKotlinMultiplatformLibrary") {
            id = "kolo.android.kotlin.multiplatform.library"
            implementationClass = "com.focus.kolo.buildlogic.android.KoloAndroidKotlinMultiplatformLibraryConventionPlugin"
        }
        register("koloRoot") {
            id = "kolo.root"
            implementationClass = "com.focus.kolo.buildlogic.root.KoloRootConventionPlugin"
        }
        register("koloAndroidApplication") {
            id = "kolo.android.application"
            implementationClass = "com.focus.kolo.buildlogic.android.KoloAndroidApplicationConventionPlugin"
        }
        register("koloComposeMultiplatform") {
            id = "kolo.compose.multiplatform"
            implementationClass = "com.focus.kolo.buildlogic.compose.KoloComposeMultiplatformConventionPlugin"
        }
        register("koloSharedMultiplatform") {
            id = "kolo.shared.multiplatform"
            implementationClass = "com.focus.kolo.buildlogic.shared.KoloSharedMultiplatformConventionPlugin"
        }
        register("koloServerJvm") {
            id = "kolo.server.jvm"
            implementationClass = "com.focus.kolo.buildlogic.server.KoloServerJvmConventionPlugin"
        }
    }
}

plugins {
    `kotlin-dsl`
}

group = "com.focus.kolo.buildlogic"

kotlin {
    jvmToolchain(17)
    explicitApi()
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

tasks.validatePlugins {
    enableStricterValidation.set(true)
}

gradlePlugin {
    plugins {
        register("koloAndroidApplication") {
            id = "kolo.android.application"
            implementationClass = "com.focus.kolo.buildlogic.KoloAndroidApplicationConventionPlugin"
        }
        register("koloComposeMultiplatform") {
            id = "kolo.compose.multiplatform"
            implementationClass = "com.focus.kolo.buildlogic.KoloComposeMultiplatformConventionPlugin"
        }
        register("koloSharedMultiplatform") {
            id = "kolo.shared.multiplatform"
            implementationClass = "com.focus.kolo.buildlogic.KoloSharedMultiplatformConventionPlugin"
        }
        register("koloServerJvm") {
            id = "kolo.server.jvm"
            implementationClass = "com.focus.kolo.buildlogic.KoloServerJvmConventionPlugin"
        }
    }
}

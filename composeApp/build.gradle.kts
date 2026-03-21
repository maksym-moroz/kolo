plugins {
    alias(libs.plugins.kolo.compose.multiplatform)
}

kotlin {
    android {
        namespace = "com.focus.kolo.composeapp"
        withHostTest {}
        androidResources {
            enable = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(projects.shared)
        }
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
        }
    }
}

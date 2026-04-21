rootProject.name = "Kolo"

pluginManagement {
    includeBuild("build-logic")

    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":androidApp")
project(":androidApp").projectDir = file("apps/reference/androidApp")
include(":baselineprofile")
include(":composeApp")
include(":debugmenu")
include(":server")
include(":shared")
include(":shared:core:appshell")
include(":shared:core:config:api")
include(":shared:core:config:impl")
include(":shared:core:store:api")
include(":shared:core:store:impl")

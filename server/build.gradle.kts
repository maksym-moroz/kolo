plugins {
    alias(libs.plugins.kolo.server.jvm)
}

group = "com.focus.kolo"
version = providers.gradleProperty("kolo.version.name").get()

application {
    mainClass.set("com.focus.kolo.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
}

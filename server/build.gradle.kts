plugins {
    alias(libs.plugins.kolo.server.jvm)
}

group = "com.focus.kolo"
version =
    providers
        .gradleProperty("kolo.version.name")
        .get()

application {
    mainClass
        .set("com.focus.kolo.ApplicationKt")

    val isDevelopment: Boolean =
        project.ext
            .has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.ktor.http)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    runtimeOnly(libs.logback)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.ktor.clientCore)
    testImplementation(libs.ktor.http)
    testImplementation(libs.ktor.serverTestHost)
    testRuntimeOnly(libs.kotlin.testJunit)
}

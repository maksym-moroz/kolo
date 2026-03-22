import java.net.URLEncoder
import java.nio.charset.StandardCharsets

val defaultPostgresHost = "localhost"
val defaultPostgresPort = 5432
val defaultPostgresTimeZone = "UTC"

plugins {
    alias(libs.plugins.kolo.server.jvm)
    alias(libs.plugins.flyway)
}

group = "com.focus.kolo"
version = providers.gradleProperty("kolo.version.name").get()

val flywayMigration by configurations.creating

fun resolvedConfigValue(key: String): String? = providers.environmentVariable(key).orNull

fun resolvedConfigInt(key: String): Int? {
    val value = resolvedConfigValue(key) ?: return null
    return value.toIntOrNull() ?: error("Configuration value '$key' must be an integer.")
}

fun postgresJdbcUrl(
    host: String,
    port: Int,
    databaseName: String,
    sessionTimeZoneId: String,
): String {
    val encodedSessionTimeZone = URLEncoder.encode(sessionTimeZoneId, StandardCharsets.UTF_8)
    return "jdbc:postgresql://$host:$port/$databaseName?options=-c%20TimeZone=$encodedSessionTimeZone"
}

val postgresHost = resolvedConfigValue("POSTGRES_HOST") ?: defaultPostgresHost
val postgresPort = resolvedConfigInt("POSTGRES_PORT") ?: defaultPostgresPort
val postgresDb = resolvedConfigValue("POSTGRES_DB")
val postgresUser = resolvedConfigValue("POSTGRES_USER")
val postgresPassword = resolvedConfigValue("POSTGRES_PASSWORD")
val postgresTimeZone = resolvedConfigValue("POSTGRES_TIME_ZONE") ?: defaultPostgresTimeZone

application {
    mainClass.set("com.focus.kolo.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

flyway {
    configurations = arrayOf(flywayMigration.name)

    if (postgresDb != null && postgresUser != null && postgresPassword != null) {
        url =
            postgresJdbcUrl(
                host = postgresHost,
                port = postgresPort,
                databaseName = postgresDb,
                sessionTimeZoneId = postgresTimeZone,
            )
        user = postgresUser
        password = postgresPassword
        schemas = arrayOf("public")
        locations = arrayOf("filesystem:src/main/resources/db/migration")
    }
}

dependencies {
    add(flywayMigration.name, libs.flyway.database.postgresql)
    add(flywayMigration.name, libs.postgres)

    implementation(projects.shared)
    implementation(libs.ktor.http)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    implementation(libs.hikari)
    implementation(libs.kotlinx.datetime)
    runtimeOnly(libs.postgres)
    runtimeOnly(libs.logback)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.ktor.clientCore)
    testImplementation(libs.ktor.http)
    testImplementation(libs.ktor.serverTestHost)
    testRuntimeOnly(libs.kotlin.testJunit)
}

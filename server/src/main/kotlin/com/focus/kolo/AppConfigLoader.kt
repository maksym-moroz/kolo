package com.focus.kolo

import io.ktor.server.config.ApplicationConfig
import kotlinx.datetime.TimeZone

object AppConfigLoader {
    fun load(
        applicationConfig: ApplicationConfig,
        environment: Map<String, String> = System.getenv(),
    ): AppConfig {
        val resolver = AppConfigResolver(applicationConfig, environment)

        return AppConfig(
            database =
                DatabaseConfig(
                    host = resolver.string("POSTGRES_HOST", "kolo.database.host"),
                    port = resolver.int("POSTGRES_PORT", "kolo.database.port"),
                    name = resolver.requiredString("POSTGRES_DB"),
                    username = resolver.requiredString("POSTGRES_USER"),
                    password = resolver.requiredString("POSTGRES_PASSWORD"),
                    sessionTimeZone = resolver.timeZone("POSTGRES_TIME_ZONE", "kolo.database.sessionTimeZone"),
                ),
        )
    }
}

private class AppConfigResolver(
    private val applicationConfig: ApplicationConfig,
    private val environment: Map<String, String>,
) {
    fun requiredString(envKey: String): String =
        environment[envKey]
            ?: error("Missing required configuration value '$envKey'.")

    fun string(envKey: String, configPath: String): String =
        environment[envKey]
            ?: applicationConfig.propertyOrNull(configPath)?.getString()
            ?: error("Missing configuration value '$envKey' or '$configPath'.")

    fun int(envKey: String, configPath: String): Int =
        string(envKey, configPath).toIntOrNull()
            ?: error("Configuration value '$envKey' or '$configPath' must be an integer.")

    fun timeZone(envKey: String, configPath: String): TimeZone = TimeZone.of(string(envKey, configPath))
}

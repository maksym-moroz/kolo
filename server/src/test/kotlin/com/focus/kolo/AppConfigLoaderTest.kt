package com.focus.kolo

import io.ktor.server.config.MapApplicationConfig
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AppConfigLoaderTest {
    private val applicationConfig =
        MapApplicationConfig(
            "kolo.database.host" to "localhost",
            "kolo.database.port" to "5432",
            "kolo.database.sessionTimeZone" to "UTC",
        )

    @Test
    fun environmentVariablesOverrideLocalFallbacksAndApplicationDefaults() {
        val appConfig =
            AppConfigLoader.load(
                applicationConfig = applicationConfig,
                environment =
                    mapOf(
                        "POSTGRES_HOST" to "db.internal",
                        "POSTGRES_PORT" to "6432",
                        "POSTGRES_DB" to "kolo_env",
                        "POSTGRES_USER" to "env_user",
                        "POSTGRES_PASSWORD" to "env_password",
                        "POSTGRES_TIME_ZONE" to "Europe/Kyiv",
                    ),
            )

        assertEquals("db.internal", appConfig.database.host)
        assertEquals(6432, appConfig.database.port)
        assertEquals("kolo_env", appConfig.database.name)
        assertEquals("env_user", appConfig.database.username)
        assertEquals("env_password", appConfig.database.password)
        assertEquals(TimeZone.of("Europe/Kyiv"), appConfig.database.sessionTimeZone)
    }

    @Test
    fun applicationDefaultsBackfillNonSecretValuesWhenEnvironmentVariablesAreMissing() {
        val appConfig =
            AppConfigLoader.load(
                applicationConfig = applicationConfig,
                environment =
                    mapOf(
                        "POSTGRES_DB" to "kolo_env",
                        "POSTGRES_USER" to "env_user",
                        "POSTGRES_PASSWORD" to "env_password",
                    ),
            )

        assertEquals("localhost", appConfig.database.host)
        assertEquals(5432, appConfig.database.port)
        assertEquals("kolo_env", appConfig.database.name)
        assertEquals("env_user", appConfig.database.username)
        assertEquals("env_password", appConfig.database.password)
        assertEquals(TimeZone.UTC, appConfig.database.sessionTimeZone)
    }

    @Test
    fun missingRequiredDatabaseCredentialsFailsFast() {
        val error =
            assertFailsWith<IllegalStateException> {
                AppConfigLoader.load(
                    applicationConfig = applicationConfig,
                    environment = emptyMap(),
                )
            }

        assertEquals("Missing required configuration value 'POSTGRES_DB'.", error.message)
    }
}

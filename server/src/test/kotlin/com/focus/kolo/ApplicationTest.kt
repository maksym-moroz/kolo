package com.focus.kolo

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() =
        testApplication {
            application {
                configureApplication(
                    appConfig =
                        AppConfig(
                            database =
                                DatabaseConfig(
                                    host = "localhost",
                                    port = 5432,
                                    name = "kolo_test",
                                    username = "postgres",
                                    password = "postgres",
                                    sessionTimeZone = TimeZone.UTC,
                                ),
                        ),
                    dataSource = null,
                )
            }
            val response = client.get("/")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals("Ktor: ${AppServices.greetingText()}", response.bodyAsText())
        }
}

package com.focus.kolo

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }
        val response =
            client
                .get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(
            "Kolo server is running.",
            response
                .bodyAsText()
        )
    }
}

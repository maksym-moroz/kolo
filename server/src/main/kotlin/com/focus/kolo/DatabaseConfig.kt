package com.focus.kolo

import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlinx.datetime.TimeZone

data class DatabaseConfig(
    val host: String,
    val port: Int,
    val name: String,
    val username: String,
    val password: String,
    val sessionTimeZone: TimeZone,
) {
    val jdbcUrl: String
        get() =
            "jdbc:postgresql://$host:$port/$name?options=-c%20TimeZone=$encodedSessionTimeZone"

    val connectionInitSql: String
        get() = "SET TIME ZONE '${sessionTimeZone.id}'"

    private val encodedSessionTimeZone: String
        get() = URLEncoder.encode(sessionTimeZone.id, StandardCharsets.UTF_8)
}

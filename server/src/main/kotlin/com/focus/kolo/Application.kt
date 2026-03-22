package com.focus.kolo

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.netty.EngineMain
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import java.util.TimeZone
import javax.sql.DataSource

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val appConfig = AppConfigLoader.load(environment.config)
    configureRuntimeTimeZone(appConfig)
    configureApplication(appConfig = appConfig, dataSource = DatabaseFactory.createDataSource(appConfig.database))
}

fun Application.configureApplication(
    appConfig: AppConfig,
    dataSource: DataSource?,
) {
    val databaseConfig = appConfig.database

    if (dataSource != null) {
        monitor.subscribe(ApplicationStarted) {
            DatabaseFactory.verifyConnection(dataSource)
            environment.log.info("Database connection pool initialized for {}", databaseConfig.jdbcUrl)
        }

        monitor.subscribe(ApplicationStopped) {
            (dataSource as? AutoCloseable)?.close()
        }
    }

    routing {
        get("/") {
            call.respondText("Ktor: ${AppServices.greetingText()}")
        }

        if (dataSource != null) {
            get("/db/health") {
                DatabaseFactory.verifyConnection(dataSource)
                call.respondText("Database: OK")
            }
        }
    }
}

private fun configureRuntimeTimeZone(appConfig: AppConfig) {
    val timeZoneId = appConfig.database.sessionTimeZone.id
    System.setProperty("user.timezone", timeZoneId)
    TimeZone.setDefault(TimeZone.getTimeZone(timeZoneId))
}

package com.focus.kolo

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

object DatabaseFactory {
    fun createDataSource(config: DatabaseConfig): HikariDataSource {
        val hikariConfig =
            HikariConfig().apply {
                jdbcUrl = config.jdbcUrl
                username = config.username
                password = config.password
                driverClassName = "org.postgresql.Driver"
                maximumPoolSize = 10
                minimumIdle = 1
                isAutoCommit = false
                transactionIsolation = "TRANSACTION_READ_COMMITTED"
                connectionInitSql = config.connectionInitSql
                validate()
            }

        return HikariDataSource(hikariConfig)
    }

    fun verifyConnection(dataSource: DataSource) {
        dataSource.connection.use { connection ->
            connection.prepareStatement("SELECT 1").use { statement ->
                statement.executeQuery().use { resultSet ->
                    check(resultSet.next()) { "Database connectivity check returned no rows." }
                }
            }
        }
    }
}

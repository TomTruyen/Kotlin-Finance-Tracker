package com.tomtruyen

import com.tomtruyen.data.table.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(environment: ApplicationEnvironment) {
        Database.connect(hikari(environment))

        transaction {
            SchemaUtils.create(UserTable)
        }
    }

    fun hikari(environment: ApplicationEnvironment): HikariDataSource {
        val user = environment.config.property("postgres.user").getString()
        val password = environment.config.property("postgres.password").getString()
        val database = environment.config.property("postgres.database").getString()

        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = "jdbc:postgresql://$database?user=$user&password=$password"
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }.also {
            it.validate()
        }

        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}
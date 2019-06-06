package ru.mbannikov.webfluxtestjdbc

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.context.support.GenericApplicationContext
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter
import org.springframework.web.server.adapter.WebHttpHandlerBuilder
import reactor.netty.http.server.HttpServer
import ru.mbannikov.webfluxtestjdbc.infrastructure.UserTable

private val logger = KotlinLogging.logger("Application")

class Server(
    host: String = "0.0.0.0",
    port: Int = 8080
) {
    private val server: HttpServer

    init {
        val context = GenericApplicationContext {
            beans().initialize(this)
            refresh()
        }
        val handler = WebHttpHandlerBuilder.applicationContext(context).build()
        val adapter = ReactorHttpHandlerAdapter(handler)

        server = HttpServer.create()
            .host(host)
            .port(port)
            .handle(adapter)
    }

    fun start() {
        logger.info { "Starting server..." }
        server.bindNow().onDispose().block()
    }
}

fun hikariConnect(url: String, driver: String, user: String, pwd: String): Database {
    val hikariConfig = HikariConfig().apply {
        jdbcUrl = url
        driverClassName = driver
        username = user
        password = pwd
    }

    val datasource = HikariDataSource(hikariConfig)

    return Database.connect(datasource)
}

fun simpleConnect(url: String, driver: String, user: String, pwd: String): Database =
    Database.connect(
        url = url,
        driver = driver,
        user = user,
        password = pwd
    )

fun main() {
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE")

    val jdbcUrl = "jdbc:postgresql://postgres:5432/fluxjdbc"
    val jdbcDriver = "org.postgresql.Driver"
    val dbUsername = "postgres"
    val dbPassword = "postgres"

    val USE_HIKARI = false
    when (USE_HIKARI) {
        true -> hikariConnect(jdbcUrl, jdbcDriver, dbUsername, dbPassword)
        false -> simpleConnect(jdbcUrl, jdbcDriver, dbUsername, dbPassword)
    }

    transaction {
        SchemaUtils.create(UserTable)
    }

    Server().start()
}
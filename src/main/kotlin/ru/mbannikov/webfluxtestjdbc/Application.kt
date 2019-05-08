package ru.mbannikov.webfluxtestjdbc

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

fun main() {
    Database.connect(
        url = "jdbc:postgresql://postgres:5432/fluxjdbc",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "postgres"
    )

    transaction {
        SchemaUtils.create(UserTable)
    }

    Server().start()
}
package ru.mbannikov.webfluxtestjdbc

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.context.support.GenericApplicationContext
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter
import org.springframework.web.server.adapter.WebHttpHandlerBuilder
import reactor.netty.http.server.HttpServer
import ru.mbannikov.webfluxtestjdbc.infrastructure.UserTable

class Server(
    host: String = "localhost",
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
        server.bindNow().onDispose().block()
    }
}

fun main() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/fluxjdbc",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "postgres"
    )

    transaction {
        SchemaUtils.create(UserTable)
    }

    Server().start()
}
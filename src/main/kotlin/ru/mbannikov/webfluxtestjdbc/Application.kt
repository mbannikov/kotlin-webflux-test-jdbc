package ru.mbannikov.webfluxtestjdbc

import mu.KotlinLogging
import org.springframework.context.support.GenericApplicationContext
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter
import org.springframework.web.server.adapter.WebHttpHandlerBuilder
import reactor.netty.http.server.HttpServer
import java.lang.IllegalArgumentException

private val logger = KotlinLogging.logger("Application")

enum class RepositoryType {
    PG_EXPOSED,
    PG_EXPOSED_NON_BLOCKING,
    PG_HIKARI,
    PG_HIKARI_NON_BLOCKING,
    MONGO
}

class Server(
    host: String = "0.0.0.0",
    port: Int = 8080,
    userRepoType: RepositoryType
) {
    private val server: HttpServer

    init {
        val context = GenericApplicationContext {
            beans(userRepoType).initialize(this)
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

private fun printHelp(): Nothing {
    val types = RepositoryType.values().joinToString(", ")
    println("./app TYPE\nTypes: $types")

    return System.exit(1) as Nothing
}

fun main(args: Array<String>) {
    if (args.size != 1)
        printHelp()

    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE")

    val type = try {
        RepositoryType.valueOf(args[0].toUpperCase())
    } catch (ex: IllegalArgumentException) {
        printHelp()
    }

    Server(userRepoType = type).start()
}
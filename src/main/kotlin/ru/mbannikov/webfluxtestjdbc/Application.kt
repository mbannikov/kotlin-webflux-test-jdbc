package ru.mbannikov.webfluxtestjdbc

import org.springframework.context.support.GenericApplicationContext
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter
import org.springframework.web.server.adapter.WebHttpHandlerBuilder
import reactor.netty.http.server.HttpServer

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
    Server().start()
}
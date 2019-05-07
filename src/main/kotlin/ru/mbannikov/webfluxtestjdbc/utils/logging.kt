package ru.mbannikov.webfluxtestjdbc.utils

import mu.KLogger
import org.springframework.web.reactive.function.server.ServerRequest

fun logRequest(logger: KLogger, request: ServerRequest, body: Any) {
    val method = request.methodName()
    val path = request.uri().toURL().file

    logger.info { "[$method - $path] - $body" }
}
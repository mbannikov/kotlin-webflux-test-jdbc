package ru.mbannikov.webfluxtestjdbc.network.handler

import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

class ProfileHandler {
    fun get(request: ServerRequest): Mono<ServerResponse> {
        var id = request.pathVariables()["id"]?.toLong()

        return ServerResponse.ok().build()
    }
}
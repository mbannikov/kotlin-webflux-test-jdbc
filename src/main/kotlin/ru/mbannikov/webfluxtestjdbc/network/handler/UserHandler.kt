package ru.mbannikov.webfluxtestjdbc.network.handler

import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

class UserHandler {
    fun login(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().build()
    }

    fun register(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().build()
    }
}
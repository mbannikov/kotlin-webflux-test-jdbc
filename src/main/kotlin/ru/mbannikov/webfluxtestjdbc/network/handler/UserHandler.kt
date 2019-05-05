package ru.mbannikov.webfluxtestjdbc.network.handler

import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import ru.mbannikov.webfluxtestjdbc.domain.UserAuthenticationService
import ru.mbannikov.webfluxtestjdbc.domain.UserRegisterService
import ru.mbannikov.webfluxtestjdbc.domain.Username
import ru.mbannikov.webfluxtestjdbc.network.dto.LoginRequest
import ru.mbannikov.webfluxtestjdbc.network.dto.RegisterRequest
import ru.mbannikov.webfluxtestjdbc.network.dto.RegisterResponse

class UserHandler(
    private val authenticationService: UserAuthenticationService,
    private val registerService: UserRegisterService
) {
    private val SUCCESS_STATUS = "success"
    private val FAIL_STATUS = "fail"

    private val successResponse = RegisterResponse(SUCCESS_STATUS)

    fun login(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(LoginRequest::class.java)
            .flatMap { authenticationService.authenticate(it.email, it.password).toMono() }
            .flatMap { ServerResponse.ok().syncBody(it) }

    }

    fun register(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(RegisterRequest::class.java)
            .flatMap { registerService.register(Username(it.username), it.email, it.password).toMono() }
            .flatMap { ServerResponse.ok().syncBody(successResponse) }
    }
}
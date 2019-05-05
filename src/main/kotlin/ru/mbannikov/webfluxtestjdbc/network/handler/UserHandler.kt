package ru.mbannikov.webfluxtestjdbc.network.handler

import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import ru.mbannikov.webfluxtestjdbc.domain.UserAuthenticationService
import ru.mbannikov.webfluxtestjdbc.domain.UserRegisterService
import ru.mbannikov.webfluxtestjdbc.domain.Username
import ru.mbannikov.webfluxtestjdbc.network.dto.*

class UserHandler(
    private val authenticationService: UserAuthenticationService,
    private val registerService: UserRegisterService
) {
    companion object {
        private const val SUCCESS_STATUS = "success"
        private const val FAIL_STATUS = "fail"
    }

    private val successResponse = RegisterResponse(SUCCESS_STATUS)

    fun login(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(LoginRequest::class.java)
            .flatMap { authenticationService.authenticate(it.email, it.password).toMono() }
            .flatMap { ServerResponse.ok().syncBody(LoginSuccessResponse(SUCCESS_STATUS, "some_jwt_token")) }
            .onErrorResume {
                val responseMessage = when (it) {
                    is BadCredentialsException -> "Bad credentials"
                    else -> "Server error"
                }
                ServerResponse.status(HttpStatus.UNAUTHORIZED).syncBody(LoginFailResponse(FAIL_STATUS, responseMessage))
            }

    }

    fun register(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(RegisterRequest::class.java)
            .flatMap { registerService.register(Username(it.username), it.email, it.password).toMono() }
            .flatMap { ServerResponse.ok().syncBody(successResponse) }
            .onErrorResume { ServerResponse.ok().syncBody(RegisterResponse(FAIL_STATUS, "Error during registration")) }
    }
}
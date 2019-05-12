package ru.mbannikov.webfluxtestjdbc.domain.user

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

class UserAuthenticationService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun authenticate(username: Username, password: String): Mono<User> {
        return userRepository.findBy(username)
            .flatMap { authenticate(it, password) }
            .switchIfEmpty(Mono.error(BadCredentialsException("Bad credentials")))
    }

    fun authenticate(email: String, password: String): Mono<User> {
        return userRepository.findByEmail(email)
            .flatMap { authenticate(it, password) }
            .switchIfEmpty(Mono.error(BadCredentialsException("Bad credentials")))
    }

    private fun authenticate(user: User, password: String): Mono<User> =
        if (passwordEncoder.matches(password, user.password))
            user.toMono()
        else
            Mono.error(BadCredentialsException("Bad credentials"))
}
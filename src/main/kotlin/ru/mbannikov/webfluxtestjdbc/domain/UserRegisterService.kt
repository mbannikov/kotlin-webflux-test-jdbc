package ru.mbannikov.webfluxtestjdbc.domain

import org.springframework.security.crypto.password.PasswordEncoder
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.switchIfEmpty

class UserRegisterService(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository
) {
    fun register(username: Username, email: String, password: String): Mono<User> {
        val byUsername = userRepository.findBy(username)
            .flatMap { Mono.error<User>(UserExistsException(username)) }

        val byEmail = userRepository.findByEmail(email)
            .flatMap { Mono.error<User>(UserExistsException(email)) }

        return Flux.merge(byUsername, byEmail)
            .singleOrEmpty()
            .switchIfEmpty {
                val user = User(username = username, password = passwordEncoder.encode(password), email = email)
                userRepository.create(user)
            }
    }
}
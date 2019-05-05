package ru.mbannikov.webfluxtestjdbc.domain

import org.springframework.security.crypto.password.PasswordEncoder

class UserRegisterService(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository
) {
    fun register(username: Username, email: String, password: String): User {
        val user = User(
            username = username,
            password = passwordEncoder.encode(password),
            email = email
        )

        return userRepository.create(user)
    }
}
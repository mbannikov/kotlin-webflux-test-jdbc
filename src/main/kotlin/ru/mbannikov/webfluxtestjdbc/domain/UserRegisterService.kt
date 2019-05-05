package ru.mbannikov.webfluxtestjdbc.domain

import org.springframework.security.crypto.password.PasswordEncoder

class UserRegisterService(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository
) {
    fun register(username: Username, email: String, password: String): User {
        checkExistence(username)
        checkExistence(email)

        val user = User(
            username = username,
            password = passwordEncoder.encode(password),
            email = email
        )

        return userRepository.create(user)
    }

    private fun checkExistence(username: Username) =
        userRepository.findBy(username)?.let { throw UserExistsException(username) }

    private fun checkExistence(email: String) =
        userRepository.findByEmail(email)?.let { throw UserExistsException(email) }
}
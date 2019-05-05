package ru.mbannikov.webfluxtestjdbc.domain

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder

class UserAuthenticationService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun authenticate(username: Username, password: String): User {
        val user = userRepository.findBy(username)

        return user?.let {
            authenticate(it, password)
        } ?: throw BadCredentialsException("Bad credentials")
    }

    fun authenticate(email: String, password: String): User {
        val user = userRepository.findByEmail(email)

        return user?.let {
            authenticate(it, password)
        } ?: throw BadCredentialsException("Bad credentials")
    }

    private fun authenticate(user: User, password: String): User? {
        return if (passwordEncoder.matches(password, user.password)) user else null
    }
}
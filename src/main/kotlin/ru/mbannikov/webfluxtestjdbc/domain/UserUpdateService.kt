package ru.mbannikov.webfluxtestjdbc.domain

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder

class UserUpdateService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun updateEmail(user: User, newEmail: String): User {
        return userRepository.getBy(user.id)
            .copy(email = newEmail)
            .also(userRepository::save)
    }

    fun updatePassword(user: User, password: String, newPassword: String, confirmPassword: String): User {
        if (!passwordEncoder.matches(password, user.password)) throw BadCredentialsException("Bad credentials")

        // TODO
//        if (newPassword != confirmPassword) throw

        return userRepository.getBy(user.id)
            .copy(password = passwordEncoder.encode(newPassword))
            .also(userRepository::save)
    }
}
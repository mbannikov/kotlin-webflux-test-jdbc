package ru.mbannikov.webfluxtestjdbc.network.dto

data class RegisterDto(
    val username: String,
    val email: String,
    val password: String
)

typealias RegisterRequest = RegisterDto

data class RegisterResponse(
    val status: String,
    val message: String? = null
)
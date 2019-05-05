package ru.mbannikov.webfluxtestjdbc.network.dto

data class LoginDto(
    val email: String,
    val password: String
)

typealias LoginRequest = LoginDto

data class LoginSuccessResponse(
    val status: String,
    val token: String
)

data class LoginFailResponse(
    val status: String,
    val message: String? = null
)

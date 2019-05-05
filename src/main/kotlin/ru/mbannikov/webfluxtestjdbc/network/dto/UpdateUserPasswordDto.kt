package ru.mbannikov.webfluxtestjdbc.network.dto

data class UpdateUserPasswordDto(
    val password: String,
    val confirmPassword: String
)
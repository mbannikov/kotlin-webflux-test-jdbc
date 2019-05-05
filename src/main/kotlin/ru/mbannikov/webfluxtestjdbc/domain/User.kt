package ru.mbannikov.webfluxtestjdbc.domain

import ru.mbannikov.webfluxtestjdbc.refs.UserId

data class User(
    val id: UserId = UserId.New,
    val username: Username,
    val password: String,
    val email: String
)

data class Username(
    val value: String
)
package ru.mbannikov.webfluxtestjdbc.domain

import ru.mbannikov.webfluxtestjdbc.refs.UserId

interface UserRepository {
    //** Read part **//
    fun findBy(userId: UserId): User?
    fun findBy(username: Username): User?
    fun findByEmail(email: String): User?

    fun getBy(userId: UserId) = findBy(userId) ?: throw UserNotFoundException(userId)
    fun getBy(username: Username) = findBy(username) ?: throw UserNotFoundException(username)

    //** Write part **//
    fun create(user: User): User
    fun save(user: User): User
}
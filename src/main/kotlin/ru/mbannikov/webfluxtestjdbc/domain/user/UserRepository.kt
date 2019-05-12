package ru.mbannikov.webfluxtestjdbc.domain.user

import reactor.core.publisher.Mono
import reactor.core.publisher.switchIfEmpty
import ru.mbannikov.webfluxtestjdbc.refs.UserId

interface UserRepository {
    //** Read part **//
    fun findBy(userId: UserId): Mono<User>
    fun findBy(username: Username): Mono<User>
    fun findByEmail(email: String): Mono<User>

    fun getBy(userId: UserId): Mono<User> = findBy(userId).switchIfEmpty { Mono.error(
        UserNotFoundException(userId)
    ) }
    fun getBy(username: Username): Mono<User> = findBy(username).switchIfEmpty { Mono.error(
        UserNotFoundException(username)
    ) }

    //** Write part **//
    fun create(user: User): Mono<User>
    fun save(user: User): Mono<User>
}
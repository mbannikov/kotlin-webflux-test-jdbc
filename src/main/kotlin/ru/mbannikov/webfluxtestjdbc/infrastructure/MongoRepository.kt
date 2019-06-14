package ru.mbannikov.webfluxtestjdbc.infrastructure

import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import ru.mbannikov.webfluxtestjdbc.domain.user.User
import ru.mbannikov.webfluxtestjdbc.domain.user.UserRepository
import ru.mbannikov.webfluxtestjdbc.domain.user.Username
import ru.mbannikov.webfluxtestjdbc.refs.UserId

class MongoRepository : UserRepository {
    private val mockUser = User(
        id = UserId.Persisted(1),
        username = Username("admin"),
        password = "100500",
        email = "admin@localhost"
    )

    override fun findBy(userId: UserId): Mono<User> {
        return mockUser.toMono()
    }

    override fun findBy(username: Username): Mono<User> {
        return mockUser.toMono()
    }

    override fun findByEmail(email: String): Mono<User> {
        return mockUser.toMono()
    }

    override fun create(user: User): Mono<User> {
        return mockUser.toMono()
    }

    override fun save(user: User): Mono<User> {
        return mockUser.toMono()
    }
}
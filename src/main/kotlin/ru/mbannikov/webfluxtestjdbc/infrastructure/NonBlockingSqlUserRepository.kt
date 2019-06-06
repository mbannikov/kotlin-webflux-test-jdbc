package ru.mbannikov.webfluxtestjdbc.infrastructure

import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import ru.mbannikov.webfluxtestjdbc.domain.user.User
import ru.mbannikov.webfluxtestjdbc.domain.user.UserRepository
import ru.mbannikov.webfluxtestjdbc.domain.user.Username
import ru.mbannikov.webfluxtestjdbc.refs.UserId

class NonBlockingSqlUserRepository(
    private val sqlRepository: SqlUserRepository
) : UserRepository {
    private val scheduler = Schedulers.newElastic("UserRepository")

//    override fun findBy(userId: UserId): Mono<User> = sqlRepository.findBy(userId).subscribeOn(scheduler)
    override fun findBy(userId: UserId): Mono<User> = Mono.fromSupplier { sqlRepository.findBy(userId).block() }.subscribeOn(scheduler)

//    override fun findBy(username: Username): Mono<User> = sqlRepository.findBy(username).subscribeOn(scheduler)
    override fun findBy(username: Username): Mono<User> = Mono.fromSupplier {  sqlRepository.findBy(username).block() }.subscribeOn(scheduler)

//    override fun findByEmail(email: String): Mono<User> = sqlRepository.findByEmail(email).subscribeOn(scheduler)
    override fun findByEmail(email: String): Mono<User> = Mono.fromSupplier { sqlRepository.findByEmail(email).block() }.subscribeOn(scheduler)

//    override fun create(user: User): Mono<User> = sqlRepository.create(user).subscribeOn(scheduler)
    override fun create(user: User): Mono<User> = Mono.fromSupplier { sqlRepository.create(user).block() }.subscribeOn(scheduler)

//    override fun save(user: User): Mono<User> = sqlRepository.save(user).subscribeOn(scheduler)
    override fun save(user: User): Mono<User> = Mono.fromSupplier { sqlRepository.save(user).block() }.subscribeOn(scheduler)
}
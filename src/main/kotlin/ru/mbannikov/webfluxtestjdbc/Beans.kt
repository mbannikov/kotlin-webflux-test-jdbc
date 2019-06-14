package ru.mbannikov.webfluxtestjdbc

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.context.support.beans
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.server.adapter.WebHttpHandlerBuilder
import ru.mbannikov.webfluxtestjdbc.domain.user.UserAuthenticationService
import ru.mbannikov.webfluxtestjdbc.domain.user.UserRegisterService
import ru.mbannikov.webfluxtestjdbc.domain.user.UserRepository
import ru.mbannikov.webfluxtestjdbc.infrastructure.MongoRepository
import ru.mbannikov.webfluxtestjdbc.infrastructure.NonBlockingSqlUserRepository
import ru.mbannikov.webfluxtestjdbc.infrastructure.SqlUserRepository
import ru.mbannikov.webfluxtestjdbc.infrastructure.UserTable
import ru.mbannikov.webfluxtestjdbc.network.Routes
import ru.mbannikov.webfluxtestjdbc.network.handler.ProfileHandler
import ru.mbannikov.webfluxtestjdbc.network.handler.UserHandler

private const val jdbcUrl = "jdbc:postgresql://localhost:5432/fluxjdbc"
private const val jdbcDriver = "org.postgresql.Driver"
private const val dbUsername = "postgres"
private const val dbPassword = "postgres"

fun beans(repoType: RepositoryType) = beans {
    /** Routes **/
    bean<Routes>()
    bean(WebHttpHandlerBuilder.WEB_HANDLER_BEAN_NAME) {
        RouterFunctions.toWebHandler(ref<Routes>().router())
    }

    /** Spring Security **/
    bean<PasswordEncoder> { BCryptPasswordEncoder() }

    /** Handlers **/
    bean<UserHandler>()
    bean<ProfileHandler>()

    /** Services **/
    bean<UserAuthenticationService>()
    bean<UserRegisterService>()

    /** Repositories **/
    bean {
        getUserRepository(repoType)
    }
}

private fun getUserRepository(type: RepositoryType) = when (type) {
    RepositoryType.PG_EXPOSED -> getPostgresUserRepository(true, false)
    RepositoryType.PG_EXPOSED_NON_BLOCKING -> getPostgresUserRepository(false, false)
    RepositoryType.PG_HIKARI -> getPostgresUserRepository(true, true)
    RepositoryType.PG_HIKARI_NON_BLOCKING -> getPostgresUserRepository(false, true)
    RepositoryType.MONGO -> getMongoUserRepository()
}

private fun getPostgresUserRepository(isBlocking: Boolean, useHikari: Boolean): UserRepository {
    when (useHikari) {
        true -> connectHikari()
        false -> connectExposed()
    }

    transaction {
        SchemaUtils.create(UserTable)
    }

    val blockingRepo = SqlUserRepository()

    return when (isBlocking) {
        true -> blockingRepo
        false -> NonBlockingSqlUserRepository(blockingRepo)
    }
}

private fun getMongoUserRepository(): UserRepository {
    return MongoRepository()
}

private fun connectExposed() {
    Database.connect(
        url = jdbcUrl,
        driver = jdbcDriver,
        user = dbUsername,
        password = dbPassword
    )
}

private fun connectHikari() {
    val hikariConfig = HikariConfig().apply {
        jdbcUrl = jdbcUrl
        driverClassName = jdbcDriver
        username = dbUsername
        password = dbPassword
    }

    Database.connect(
        HikariDataSource(hikariConfig)
    )
}
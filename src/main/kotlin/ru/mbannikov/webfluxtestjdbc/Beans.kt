package ru.mbannikov.webfluxtestjdbc

import org.springframework.context.support.beans
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.server.adapter.WebHttpHandlerBuilder
import ru.mbannikov.webfluxtestjdbc.domain.UserAuthenticationService
import ru.mbannikov.webfluxtestjdbc.domain.UserRegisterService
import ru.mbannikov.webfluxtestjdbc.domain.UserRepository
import ru.mbannikov.webfluxtestjdbc.infrastructure.NonBlockingSqlUserRepository
import ru.mbannikov.webfluxtestjdbc.infrastructure.SqlUserRepository
import ru.mbannikov.webfluxtestjdbc.network.Routes
import ru.mbannikov.webfluxtestjdbc.network.handler.ProfileHandler
import ru.mbannikov.webfluxtestjdbc.network.handler.UserHandler

fun beans() = beans {
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
    bean<SqlUserRepository>()
    bean<NonBlockingSqlUserRepository>()
    bean<UserRepository> {
        ref<SqlUserRepository>()
//        ref<NonBlockingSqlUserRepository>()
    }
}
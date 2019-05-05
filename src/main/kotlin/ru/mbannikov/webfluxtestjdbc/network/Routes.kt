package ru.mbannikov.webfluxtestjdbc.network

import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router
import ru.mbannikov.webfluxtestjdbc.network.handler.ProfileHandler
import ru.mbannikov.webfluxtestjdbc.network.handler.UserHandler

class Routes(
    private val userHandler: UserHandler,
    private val profileHandler: ProfileHandler
) {
    fun router() = router {
        accept(MediaType.APPLICATION_JSON_UTF8).nest {
            POST("/login", userHandler::login)
            POST("/register", userHandler::register)

            "/profile".nest {
                GET("", profileHandler::get)
                GET("/{id}", profileHandler::get)
            }
        }
    }
}

package ru.mbannikov.webfluxtestjdbc

import org.springframework.context.support.beans
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.server.adapter.WebHttpHandlerBuilder
import ru.mbannikov.webfluxtestjdbc.network.Routes
import ru.mbannikov.webfluxtestjdbc.network.handler.ProfileHandler
import ru.mbannikov.webfluxtestjdbc.network.handler.UserHandler

fun beans() = beans {
    bean<UserHandler>()
    bean<ProfileHandler>()
    bean<Routes>()

    bean(WebHttpHandlerBuilder.WEB_HANDLER_BEAN_NAME) {
        RouterFunctions.toWebHandler(ref<Routes>().router())
    }
}
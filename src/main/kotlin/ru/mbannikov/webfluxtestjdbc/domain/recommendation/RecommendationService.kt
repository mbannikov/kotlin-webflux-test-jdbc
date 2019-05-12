package ru.mbannikov.webfluxtestjdbc.domain.recommendation

import reactor.core.publisher.Mono
import ru.mbannikov.webfluxtestjdbc.domain.user.User

class RecommendationService {
    // TODO: add webclient for recommendation microservice

    fun recommend(user: User): Mono<Recommendation> {
        val recommendation = Recommendation(
            films = listOf(),
            books = listOf(),
            articles = listOf()
        )

        return Mono.just(recommendation)
    }
}
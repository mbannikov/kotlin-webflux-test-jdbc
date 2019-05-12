package ru.mbannikov.webfluxtestjdbc.domain.recommendation

data class Recommendation(
    val films: Collection<Film>,
    val books: Collection<Book>,
    val articles: Collection<Article>
)
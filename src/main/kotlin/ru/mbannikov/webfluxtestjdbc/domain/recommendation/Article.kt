package ru.mbannikov.webfluxtestjdbc.domain.recommendation

import org.joda.time.DateTime

data class Article(
    val title: String,
    val posted: DateTime
)
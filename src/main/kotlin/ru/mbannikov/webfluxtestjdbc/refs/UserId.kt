package ru.mbannikov.webfluxtestjdbc.refs

import ru.mbannikov.webfluxtestjdbc.infrastructure.GeneratedId
import ru.mbannikov.webfluxtestjdbc.infrastructure.IdNotPersistedDelegate

sealed class UserId : GeneratedId<Long>() {
    object New : UserId() {
        override val value: Long by IdNotPersistedDelegate<Long>()
    }

    data class Persisted(override val value: Long) : UserId() {
        override fun toString() = "UserId(value=$value)"
    }
}
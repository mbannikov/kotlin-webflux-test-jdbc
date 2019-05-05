package ru.mbannikov.webfluxtestjdbc.infrastructure

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import ru.mbannikov.webfluxtestjdbc.domain.User
import ru.mbannikov.webfluxtestjdbc.domain.UserRepository
import ru.mbannikov.webfluxtestjdbc.domain.Username
import ru.mbannikov.webfluxtestjdbc.refs.UserId

class SqlUserRepository : UserRepository {
    override fun findBy(userId: UserId): User? = transaction {
        UserTable.select { UserTable.id eq userId.value }.singleOrNull()?.toUser()
    }

    override fun findBy(username: Username): User? = transaction {
        UserTable.select { UserTable.username eq username.value }.singleOrNull()?.toUser()
    }

    override fun findByEmail(email: String): User? = transaction {
        UserTable.select { UserTable.email eq email }.singleOrNull()?.toUser()
    }

    override fun create(user: User): User = transaction {
        UserTable.insert { it.from(user) }
            .get(UserTable.id)
            .let { requireNotNull(it) }
            .let { user.copy(id = it.toUserId()) }
    }

    override fun save(user: User): User = transaction {
        user.apply {
            UserTable.update({ UserTable.id eq user.id.value }) {
                it.from(user)
            }
        }
    }
}

object UserTable : Table("users") {
    val id = long("id").primaryKey().autoIncrement()
    val username = text("username").uniqueIndex()
    val email = text("email").uniqueIndex()
    val password = text("password")
}

private fun ResultRow.toUser() = User(
    id = this[UserTable.id].toUserId(),
    username = this[UserTable.username].toUsername(),
    password = this[UserTable.password],
    email = this[UserTable.email]
)

private fun UpdateBuilder<Any>.from(user: User) {
    this[UserTable.username] = user.username.value
    this[UserTable.password] = user.password
    this[UserTable.email] = user.email
}

private fun Long.toUserId(): UserId = UserId.Persisted(this)

private fun String.toUsername(): Username = Username(this)
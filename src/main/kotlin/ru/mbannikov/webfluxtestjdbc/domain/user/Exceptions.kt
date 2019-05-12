package ru.mbannikov.webfluxtestjdbc.domain.user

import ru.mbannikov.webfluxtestjdbc.refs.UserId

open class ApplicationException(message: String) : RuntimeException(message)

class UserNotFoundException : ApplicationException {
    constructor(userId: UserId) : super("User with id ${userId.value} not found")
    constructor(username: Username) : super("User with username ${username.value} not found")
}

class UserExistsException : ApplicationException {
    constructor(username: Username) : super("User with username ${username.value} already exists")
    constructor(email: String) : super("User with email $email already exists")
}

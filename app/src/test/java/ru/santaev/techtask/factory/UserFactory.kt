package ru.santaev.techtask.factory

import ru.santaev.techtask.feature.gallery.domain.entity.User
import ru.santaev.techtask.network.entities.UserEntity

object UserFactory {

    fun createUserEntity(
        id: Long = 0,
        name: String = StringFactory.create(),
        email: String = StringFactory.create()
    ): UserEntity {
        return UserEntity(
            id = id,
            name = name,
            email = email
        )
    }

    fun createUser(
        id: Long = 0,
        name: String = StringFactory.create(),
        email: String = StringFactory.create(),
        createdAt: Long = 0
    ): User {
        return User(
            id = id,
            name = name,
            email = email,
            createdAt = createdAt
        )
    }
}
package com.example.manager

import com.example.model.User
import com.example.model.UserResponse

class UserManager {
    private val users = mutableListOf<User>()

    fun register(name: String, password: String): User {
        if (users.any { it.name == name }) throw IllegalArgumentException("Такой пользователь уже существует")
        val user = User(name = name, password = password)

        users.add(user)
        return user
    }

    fun login(name: String, password: String): User? {
        return users.find { it.name == name && it.password == password }
    }

    fun read(): List<UserResponse> {
        return users.map { UserResponse(it.id, it.name) }.toList()
    }
}
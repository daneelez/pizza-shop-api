package com.example.controller

import com.example.manager.UserManager
import com.example.model.*

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes(userManager: UserManager) {
    val PREFIX = "user"

    post("$PREFIX/register") {
        val request = call.receive<RegisterRequest>()
        try {
            val user = userManager.register(request.name, request.password)
            call.respond(LoginResponse(user.id, user.name))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Ошибка регистрации!")
        }
    }

    post("$PREFIX/login") {
        val request = call.receive<LoginRequest>()
        try {
            val user = userManager.login(request.name, request.password)
            if (user != null) {
                call.respond(LoginResponse(user.id, user.name))
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Неверный логин или пароль")
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Ошибка входа в аккаунт!")
        }
    }

    get("$PREFIX/read") {
        val users = UserReadResponse(userManager.read())
        call.respond(users)
    }
}
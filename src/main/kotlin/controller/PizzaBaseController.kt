package com.example.controller

import com.example.manager.PizzaBaseManager
import com.example.model.*

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.pizzaBasesRoutes(pizzaBaseManager: PizzaBaseManager) {
    val PREFIX = "bases"

    post("$PREFIX/create") {
        val request = call.receive<PizzaBaseRequest>()
        try {
            val pizzaBaseProps = PizzaBaseProps(request.name, request.price)
            val pizzaBase = pizzaBaseManager.create(request.id, pizzaBaseProps)

            val bases = pizzaBaseManager.read(request.id)
            call.respond(bases)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Ошибка создания основы!")
        }
    }

    post("$PREFIX/update") {
        val request = call.receive<PizzaBaseUpdateRequest>()
        try {
            val pizzaBaseProps = PizzaBaseProps(request.name, request.price)

            pizzaBaseManager.update(request.id, request.itemId, pizzaBaseProps)

            val bases = pizzaBaseManager.read(request.id)
            call.respond(bases)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Ошибка обновления основы!")
        }
    }

    post("$PREFIX/delete") {
        val request = call.receive<PizzaBaseDeleteRequest>()
        try {
            pizzaBaseManager.delete(request.id, request.itemId)

            val bases = pizzaBaseManager.read(request.id)
            call.respond(bases)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Ошибка удаления основы!")
        }
    }

    post("$PREFIX/read") {
        val request = call.receive<PizzaBaseReadRequest>()
        try {
            val bases = pizzaBaseManager.read(request.id, request.filterData)
            call.respond(bases)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Ошибка вывода основ!")
        }
    }
}
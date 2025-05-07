package com.example.controller

import com.example.manager.PizzaManager
import com.example.manager.PizzaSideManager
import com.example.model.*

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.pizzaRoutes(pizzaManager: PizzaManager) {
    val PREFIX = "pizza"

    post("$PREFIX/create") {
        val request = call.receive<PizzaRequest>()
        try {
            val pizzaProps = PizzaProps(
                request.name,
                request.ingredients,
                request.base,
                request.side,
            )

            pizzaManager.create(request.id, pizzaProps)

            val rawPizzas = pizzaManager.read(request.id)
            val pizzas = rawPizzas.map { PizzaSerializable(it) }
            call.respond(pizzas)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Ошибка создания пиццы!")
        }
    }

    post("$PREFIX/update") {
        val request = call.receive<PizzaUpdateRequest>()
        try {
            val pizzaProps = PizzaProps(
                request.name,
                request.ingredients,
                request.base,
                request.side,
            )

            pizzaManager.update(request.id, request.itemId, pizzaProps)

            val rawPizzas = pizzaManager.read(request.id)
            val pizzas = rawPizzas.map { PizzaSerializable(it) }
            call.respond(pizzas)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Ошибка обновления пиццы!")
        }
    }

    post("$PREFIX/delete") {
        val request = call.receive<PizzaDeleteRequest>()
        try {
            pizzaManager.delete(request.id, request.itemId)

            val rawPizzas = pizzaManager.read(request.id)
            val pizzas = rawPizzas.map { PizzaSerializable(it) }
            call.respond(pizzas)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Ошибка удаления пиццы!")
        }
    }

    post("$PREFIX/read") {
        val request = call.receive<PizzaReadRequest>()
        try {
            val rawPizzas = pizzaManager.read(request.id, request.filterData)
            val pizzas = rawPizzas.map { PizzaSerializable(it) };
            call.respond(pizzas)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Ошибка вывода пицц!")
        }
    }
}
package com.example.controller

import com.example.manager.PizzaSideManager
import com.example.model.*

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.pizzaSidesRoutes(pizzaSideManager: PizzaSideManager) {
    val PREFIX = "sides"

    post("$PREFIX/create") {
        val request = call.receive<PizzaSideRequest>()
        try {
            val pizzaSideProps = PizzaSideProps(
                request.name,
                request.ingredients,
                request.notAllowedList
            )

            pizzaSideManager.create(request.id, pizzaSideProps)

            val rawSides = pizzaSideManager.read(request.id)
            val sides = rawSides.map { PizzaSideSerializable(it) }
            call.respond(sides)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Ошибка создания бортика!")
        }
    }

    post("$PREFIX/update") {
        val request = call.receive<PizzaSideUpdateRequest>()
        try {
            val pizzaSideProps = PizzaSideProps(
                request.name,
                request.ingredients,
                request.notAllowedList
            )

            pizzaSideManager.update(request.id, request.itemId, pizzaSideProps)

            val rawSides = pizzaSideManager.read(request.id)
            val sides = rawSides.map { PizzaSideSerializable(it) }
            call.respond(sides)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Ошибка обновления бортика!")
        }
    }

    post("$PREFIX/delete") {
        val request = call.receive<PizzaSideDeleteRequest>()
        try {
            pizzaSideManager.delete(request.id, request.itemId)

            val rawSides = pizzaSideManager.read(request.id)
            val sides = rawSides.map { PizzaSideSerializable(it) }
            call.respond(sides)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Ошибка удаления бортика!")
        }
    }

    post("$PREFIX/read") {
        val request = call.receive<PizzaSideReadRequest>()
        try {
            val rawSides = pizzaSideManager.read(request.id, request.filterData)
            val sides = rawSides.map { PizzaSideSerializable(it) };
            call.respond(sides)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Ошибка вывода бортиков!")
        }
    }
}
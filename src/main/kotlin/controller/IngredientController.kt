package com.example.controller

import com.example.manager.IngredientManager
import com.example.model.*

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.ingredientsRoutes(ingredientManager: IngredientManager) {
    val PREFIX = "ingredients"

    post("$PREFIX/create") {
        val request = call.receive<IngredientRequest>()
        try {
            val ingredientProps = IngredientProps(request.name, request.price)
            val ingredient = ingredientManager.create(request.id, ingredientProps)

            val ingredients = ingredientManager.read(request.id)
            call.respond(ingredients)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Ошибка создания ингредиента!")
        }
    }

    post("$PREFIX/update") {
        val request = call.receive<IngredientUpdateRequest>()
        try {
            val ingredientProps = IngredientProps(request.name, request.price)

            ingredientManager.update(request.id, request.itemId, ingredientProps)

            val ingredients = ingredientManager.read(request.id)
            call.respond(ingredients)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Ошибка обновления ингредиента!")
        }
    }

    post("$PREFIX/delete") {
        val request = call.receive<IngredientDeleteRequest>()
        try {
            ingredientManager.delete(request.id, request.itemId)

            val ingredients = ingredientManager.read(request.id)
            call.respond(ingredients)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Ошибка удаления ингредиента!")
        }
    }

    post("$PREFIX/read") {
        val request = call.receive<IngredientReadRequest>()
        try {
            val ingredients = ingredientManager.read(request.id, request.filterData)
            call.respond(ingredients)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Ошибка вывода ингредиентов!")
        }
    }
}
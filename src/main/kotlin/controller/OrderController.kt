package com.example.controller

import com.example.manager.OrderManager
import com.example.model.*

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.orderRoutes(orderManager: OrderManager) {
    val PREFIX = "order"

    post("$PREFIX/create") {
        try {
            val request = call.receive<OrderRequest>()
            val validPizzas = request.pizzas.map { pizza -> PizzaOrderProps(request.owner, pizza) }.toMutableList()
            validPizzas.forEach { pizza -> pizza.validate() }

            val orderProps = OrderProps(
                owners = request.owners,
                pizzas = validPizzas,
                description = request.description,
                date = request.date,
            )

            val order = OrderSerializable(orderManager.create(request.owner, orderProps));

            call.respond(order)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Ошибка создания заказа!")
        }
    }

    post("$PREFIX/update") {
        val request = call.receive<OrderUpdateRequest>()
        try {
            val newProps = OrderProps(
                description = request.description,
                date = request.date,
            )

            orderManager.update(request.owner.id, request.id, newProps)

            val rawOrders = orderManager.read(request.owner.id)
            val orders = rawOrders.map { OrderSerializable(it) };
            call.respond(orders)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Ошибка обновления заказа!")
        }
    }

    post("$PREFIX/delete") {
        val request = call.receive<OrderDeleteRequest>()
        try {
            orderManager.delete(request.owner.id, request.id)

            val rawOrders = orderManager.read(request.owner.id)
            val orders = rawOrders.map { OrderSerializable(it) };
            call.respond(orders)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Ошибка удаления заказа!")
        }
    }


    post("$PREFIX/read") {
        val request = call.receive<OrderReadRequest>()
        try {
            val rawOrders = orderManager.read(request.id.id, request.filterData)
            val orders = rawOrders.map { OrderSerializable(it) };
            call.respond(orders)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Ошибка вывода заказов!")
        }
    }
}
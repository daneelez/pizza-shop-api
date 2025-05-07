package com.example

import com.example.manager.IngredientManager
import com.example.manager.PizzaBaseManager
import com.example.manager.PizzaManager
import com.example.manager.PizzaSideManager
import com.example.manager.UserManager
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.http.*

fun main() {

    val userManager = UserManager()
    val ingredientManager = IngredientManager()
    val pizzaBaseManager = PizzaBaseManager()
    val pizzaSideManager = PizzaSideManager()
    val pizzaManager = PizzaManager()

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module(userManager, ingredientManager, pizzaBaseManager, pizzaSideManager, pizzaManager)
    }.start(wait = true)
}

fun Application.module(
    userManager: UserManager,
    ingredientManager: IngredientManager,
    pizzaBaseManager: PizzaBaseManager,
    pizzaSideManager: PizzaSideManager,
    pizzaManager: PizzaManager,
) {
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
    }

    configureSerialization()
    configureRouting(userManager, ingredientManager, pizzaBaseManager, pizzaSideManager, pizzaManager)
}

package com.example

import com.example.controller.ingredientsRoutes
import com.example.controller.orderRoutes
import com.example.controller.pizzaBasesRoutes
import com.example.controller.pizzaRoutes
import com.example.controller.pizzaSidesRoutes
import com.example.controller.userRoutes
import com.example.manager.IngredientManager
import com.example.manager.OrderManager
import com.example.manager.PizzaBaseManager
import com.example.manager.PizzaManager
import com.example.manager.PizzaSideManager
import com.example.manager.UserManager

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userManager: UserManager,
    ingredientManager: IngredientManager,
    pizzaBaseManager: PizzaBaseManager,
    pizzaSideManager: PizzaSideManager,
    pizzaManager: PizzaManager,
    orderManager: OrderManager
) {
    routing {
        userRoutes(userManager)
        ingredientsRoutes(ingredientManager)
        pizzaBasesRoutes(pizzaBaseManager)
        pizzaSidesRoutes(pizzaSideManager)
        pizzaRoutes(pizzaManager)
        orderRoutes(orderManager)
    }
}

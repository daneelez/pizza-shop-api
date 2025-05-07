package com.example.manager

import com.example.model.Pizza
import com.example.model.PizzaBase
import com.example.model.PizzaProps
import com.example.model.PizzaSide
import com.example.model.PizzaSideFilterProps
import com.example.model.PizzaSideSerializable
import java.util.UUID

class PizzaManager : BaseManager<Pizza, PizzaProps, PizzaSideFilterProps> {

    private val userPizzas = mutableMapOf<UUID, MutableList<Pizza>>()

    override fun create(userId: UUID, values: PizzaProps): Pizza? {
        values.validate()

        val pizza = Pizza(userId, values)
        val list = userPizzas.getOrPut(userId) { mutableListOf() }

        list.add(pizza)
        return pizza
    }

    override fun update(userId: UUID, itemId: UUID, values: PizzaProps) {
        val list = userPizzas[userId] ?: return
        val item = list.find { it.id == itemId } ?: return

        values.name?.takeIf { it.isNotBlank() }?.let { item.name = it }
        item.ingredients = values.ingredients
        item.base = values.base ?: PizzaBase(userId, item.base.price)

        if (values.side === null) {
            item.side = PizzaSideSerializable(PizzaSide(userId));
        } else {
            values.side.let {
                val pizzasIds = it.notAllowedList.map { pizza -> pizza.id }
                if (itemId !in pizzasIds) {
                    item.side = it
                } else {
                    item.side = PizzaSideSerializable(PizzaSide(userId));
                }
            }
        }
    }

    override fun delete(userId: UUID, itemId: UUID) {
        userPizzas[userId]?.removeIf { it.id == itemId }
    }

    override fun read(
        userId: UUID,
        filterData: PizzaSideFilterProps?
    ): List<Pizza> {
        val list = userPizzas[userId]?.toList() ?: emptyList()

        return if (filterData != null) {
            list.filter {
                it.price >= filterData.minPrice && it.price <= filterData.maxPrice
                        && it.ingredients.containsAll(filterData.ingredients)
            }
        } else {
            list
        }
    }
}
package com.example.manager

import com.example.model.Ingredient
import com.example.model.IngredientProps
import com.example.model.PriceFilterProps

import java.util.UUID
import kotlin.math.roundToInt

class IngredientManager() : BaseManager<Ingredient, IngredientProps, PriceFilterProps> {

    private val userIngredients = mutableMapOf<UUID, MutableList<Ingredient>>()

    override fun create(userId: UUID, values: IngredientProps): Ingredient? {
        values.validate()

        val ingredient = Ingredient(userId, values)
        val list = userIngredients.getOrPut(userId) { mutableListOf() }

        list.add(ingredient)
        return ingredient
    }

    override fun update(userId: UUID, itemId: UUID, values: IngredientProps) {
        val list = userIngredients[userId] ?: return
        val item = list.find { it.id == itemId } ?: return

        values.name?.takeIf { it.isNotBlank() }?.let { item.name = it }
        values.price?.takeIf { it >= 0 }?.let { item.price = ((it * 100).roundToInt() / 100.0) }
    }

    override fun delete(userId: UUID, itemId: UUID) {
        userIngredients[userId]?.removeIf { it.id == itemId }
    }

    override fun read(userId: UUID, filterData: PriceFilterProps?): List<Ingredient> {
        val list = userIngredients[userId]?.toList() ?: emptyList()

        return if (filterData != null) {
            list.filter {
                it.price >= filterData.minPrice && it.price <= filterData.maxPrice
            }
        } else {
            list
        }
    }

    fun findById(userId: UUID, id: UUID): Ingredient? {
        return userIngredients[userId]?.find { it.id == id }
    }
}
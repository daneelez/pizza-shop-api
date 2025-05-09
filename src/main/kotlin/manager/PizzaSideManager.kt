package com.example.manager

import com.example.model.PizzaSide
import com.example.model.PizzaSideFilterProps
import com.example.model.PizzaSideProps

import java.util.UUID

class PizzaSideManager() : BaseManager<PizzaSide, PizzaSideProps, PizzaSideFilterProps, UUID> {

    private val userSides = mutableMapOf<UUID, MutableList<PizzaSide>>()

    override fun create(userId: UUID, values: PizzaSideProps): PizzaSide? {
        values.validate()

        val side = PizzaSide(userId, values)
        val list = userSides.getOrPut(userId) { mutableListOf() }

        list.add(side)
        return side
    }

    override fun update(userId: UUID, itemId: UUID, values: PizzaSideProps) {
        val list = userSides[userId] ?: return
        val item = list.find { it.id == itemId } ?: return

        values.name?.takeIf { it.isNotBlank() }?.let { item.name = it }
        item.ingredients = values.ingredients
        item.notAllowedList = values.notAllowedList
    }

    override fun delete(userId: UUID, itemId: UUID) {
        userSides[userId]?.removeIf { it.id == itemId }
    }

    override fun read(userId: UUID, filterData: PizzaSideFilterProps?): List<PizzaSide> {
        val list = userSides[userId]?.toList() ?: emptyList()

        return if (filterData != null) {
            list.filter {
                it.price >= filterData.minPrice && it.price <= filterData.maxPrice
                        && it.ingredients.containsAll(filterData.ingredients)
            }
        } else {
            list
        }
    }

    fun findById(userId: UUID, id: UUID): PizzaSide? {
        return userSides[userId]?.find { it.id == id }
    }
}
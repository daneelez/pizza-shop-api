package com.example.manager

import com.example.model.PizzaBase
import com.example.model.PizzaBaseProps
import com.example.model.PriceFilterProps
import com.example.utils.isClassicBase

import java.util.UUID
import kotlin.math.roundToInt

class PizzaBaseManager() : BaseManager<PizzaBase, PizzaBaseProps, PriceFilterProps> {

    private val userBases = mutableMapOf<UUID, MutableList<PizzaBase>>()
    private var defaultPrice: Double? = null;

    override fun create(userId: UUID, values: PizzaBaseProps): PizzaBase? {
        values.validate()

        require(
            !isClassicBase(values.name) ||
                    (isClassicBase(values.name) && defaultPrice == null)
        ) { "Основа уже есть!" }

        if (defaultPrice == null && isClassicBase(values.name)) {
            defaultPrice = values.price?.let {
                (it * 100).roundToInt() / 100.0
            } ?: 0.0
        }

        requireNotNull(defaultPrice) { "Нет основы!" }
        values.price?.let {
            require(it <= defaultPrice!! * 1.2)
            { "Цена слишком высокая! (${values.price} > ${defaultPrice!! * 1.2})" }
        }

        val base = PizzaBase(userId, values)
        val list = userBases.getOrPut(userId) { mutableListOf() }

        list.add(base)
        return base
    }

    override fun update(userId: UUID, itemId: UUID, values: PizzaBaseProps) {
        val list = userBases[userId] ?: return
        val item = findById(userId, itemId) ?: return

        if (isClassicBase(values.name) && (values.price == null || values.price > 0)) {
            val lastDefault = list.firstOrNull {
                isClassicBase(it.name)
                        && it.price == defaultPrice && it.id != itemId
            }

            defaultPrice = values.price?.let {
                (it * 100).roundToInt() / 100.0
            } ?: item.price

            list.forEach { it.price = if (it.price > defaultPrice!! * 1.2) defaultPrice!! * 1.2 else it.price }

            if (lastDefault != null) {
                delete(userId, lastDefault.id)
            }
        }

        values.name?.takeIf { it.isNotBlank() }?.let { item.name = it }
        values.price?.takeIf { it <= defaultPrice!! * 1.2 && it >= 0 }
            ?.let { item.price = ((it * 100).roundToInt() / 100.0) }
    }

    override fun delete(userId: UUID, itemId: UUID) {
        val item = findById(userId, itemId) ?: return

        require(
            !isClassicBase(item.name) ||
                    (isClassicBase(item.name) && item.price != defaultPrice)
        ) { "Нельзя удалить классическую основу!" }
        userBases[userId]?.removeIf { it.id == item.id }
    }

    override fun read(userId: UUID, filterData: PriceFilterProps?): List<PizzaBase> {
        val list = userBases[userId]?.toList() ?: emptyList()

        return if (filterData != null) {
            list.filter {
                it.price >= filterData.minPrice && it.price <= filterData.maxPrice
            }
        } else {
            list
        }
    }

    fun findById(userId: UUID, id: UUID): PizzaBase? {
        return userBases[userId]?.find { it.id == id }
    }
}
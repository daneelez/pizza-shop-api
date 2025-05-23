package com.example.manager

import com.example.model.Order
import com.example.model.OrderFilterProps
import com.example.model.OrderProps
import com.example.model.UserResponse
import com.example.utils.checkDate
import com.example.utils.priceAndDateFilter
import java.time.Instant
import java.util.UUID

class OrderManager : BaseManager<Order, OrderProps, OrderFilterProps, UserResponse> {

    private val userOrders = mutableMapOf<UUID, MutableList<Order>>()

    override fun create(userID: UserResponse, values: OrderProps): Order {
        val validatedDate = if (checkDate(values.date)) values.date else Instant.now()

        val order = Order(
            id = UUID.randomUUID(),
            owner = userID,
            owners = values.owners,
            pizzas = values.pizzas,
            description = values.description,
            date = validatedDate
        )

        order.owners.forEach { owner ->
            userOrders.computeIfAbsent(owner.id) { mutableListOf() }.add(order)
        }

        return order
    }

    override fun update(userId: UUID, itemId: UUID, values: OrderProps) {
        val list = userOrders[userId] ?: return
        val item = list.find { it.id == itemId } ?: return

        item.description = values.description
        item.date = if (checkDate(values.date)) values.date else Instant.now()
    }

    override fun delete(userId: UUID, itemId: UUID) {
        userOrders[userId]?.removeIf { it.id == itemId }
    }


    override fun read(
        userId: UUID,
        filterData: OrderFilterProps?
    ): List<Order> {
        val list = userOrders[userId]?.toList() ?: emptyList()

        return if (filterData != null) {
            list.priceAndDateFilter(
                filterData.minPrice,
                filterData.maxPrice,
                filterData.dateStart,
                filterData.dateEnd
            )
        } else {
            list
        }
    }
}
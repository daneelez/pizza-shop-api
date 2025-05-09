package com.example.model

import com.example.utils.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class PriceFilterProps(
    val minPrice: Double,
    val maxPrice: Double,
)

@Serializable
data class PizzaSideFilterProps(
    val minPrice: Double,
    val maxPrice: Double,
    val ingredients: MutableList<Ingredient> = mutableListOf(),
)

@Serializable
data class PizzaFilterProps(
    val minPrice: Double,
    val maxPrice: Double,
    val ingredients: MutableList<Ingredient> = mutableListOf(),
    val bases: MutableList<PizzaBase> = mutableListOf(),
    val sides: MutableList<PizzaSideSerializable> = mutableListOf(),
)

@Serializable
data class OrderFilterProps(
    val minPrice: Double,
    val maxPrice: Double,
    @Serializable(with = InstantSerializer::class) val dateStart: Instant,
    @Serializable(with = InstantSerializer::class) val dateEnd: Instant,
)
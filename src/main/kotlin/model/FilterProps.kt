package com.example.model

import kotlinx.serialization.Serializable

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
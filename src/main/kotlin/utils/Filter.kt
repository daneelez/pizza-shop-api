package com.example.utils

import com.example.model.Dated
import com.example.model.Ingredient
import com.example.model.Ingrediented
import com.example.model.PriceReadable
import java.time.Instant

fun <T : PriceReadable> List<T>.priceFilter(minPrice: Double, maxPrice: Double): List<T> {
    return this.filter { it.price in minPrice..maxPrice }
}

fun <T> List<T>.priceAndIngredientFilter(
    minPrice: Double,
    maxPrice: Double,
    requiredIngredients: List<Ingredient>
): List<T> where T : PriceReadable, T : Ingrediented {
    return this.filter {
        it.price in minPrice..maxPrice &&
                it.ingredients.containsAll(requiredIngredients)
    }
}

fun <T> List<T>.priceAndDateFilter(
    minPrice: Double,
    maxPrice: Double,
    dateStart: Instant,
    dateEnd: Instant,
): List<T> where T : PriceReadable, T : Dated {
    return this.filter {
        it.price in minPrice..maxPrice &&
                it.date in dateStart..dateEnd
    }
}

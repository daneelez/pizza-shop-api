package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class PizzaSliceProps(
    val name: String,
    val base: PizzaBase,
    val side: PizzaSideSerializable,
    val ingredients: MutableList<Ingredient> = mutableListOf(),
) {
    constructor(owner: UserResponse, props: PizzaSliceRequest) : this(
        name = props.name ?: "custom",
        base = props.base,
        side = props.side ?: PizzaSideSerializable(PizzaSide(owner.id)),
        ingredients = props.ingredients,
    )
}

@Serializable
data class PizzaSliceRequest(
    val name: String? = null,
    val base: PizzaBase,
    val side: PizzaSideSerializable? = null,
    val ingredients: MutableList<Ingredient> = mutableListOf(),
)
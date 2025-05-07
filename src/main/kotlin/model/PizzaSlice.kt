package com.example.model

import com.example.utils.UUIDSerializer
import com.example.utils.Validatable
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class PizzaSlice(
    @Serializable(with = UUIDSerializer::class) val id: UUID = UUID.randomUUID(),
    var name: String,
    var ingredients: List<Ingredient>,
) {
    constructor(props: PizzaSliceProps) : this(
        UUID.randomUUID(),
        props.name.orEmpty(),
        props.ingredients ?: emptyList()
    )
}

@Serializable
data class PizzaSliceProps(
    val name: String? = null,
    val ingredients: List<Ingredient>? = null,
) : Validatable {
    override fun validate() {
        require(!name.isNullOrBlank()) { "Нужно указать название!" }
        require(!ingredients.isNullOrEmpty()) { "Нужно указать ингредиенты!" }
    }
}
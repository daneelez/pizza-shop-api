package com.example.model

import com.example.utils.UUIDSerializer
import com.example.utils.Validatable
import kotlinx.serialization.Serializable
import java.util.UUID
import kotlin.math.roundToInt

@Serializable
data class Ingredient(
    @Serializable(with = UUIDSerializer::class) val id: UUID = UUID.randomUUID(),
    @Serializable(with = UUIDSerializer::class) val owner: UUID,
    internal var name: String,
    internal var price: Double,
) {

    constructor(owner: UUID, props: IngredientProps) : this(
        id = UUID.randomUUID(),
        owner = owner,
        props.name.orEmpty(),
        price = props.price?.let {
            (it * 100).roundToInt() / 100.0
        } ?: 0.0
    )
}

@Serializable
data class IngredientProps(
    val name: String? = null,
    val price: Double? = null,
) : Validatable {
    override fun validate() {
        val actualPrice = requireNotNull(price) { "Нужно указать цену!" }
        require(actualPrice >= 0) { "Нужно указать правильную цену!" }

        require(!name.isNullOrBlank()) { "Нужно указать название!" }
    }
}

@Serializable
data class IngredientRequest(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String,
    val price: Double,
)

@Serializable
data class IngredientUpdateRequest(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val itemId: UUID,
    val name: String,
    val price: Double,
)

@Serializable
data class IngredientReadRequest(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val filterData: PriceFilterProps? = null,
)

@Serializable
data class IngredientDeleteRequest(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val itemId: UUID,
)
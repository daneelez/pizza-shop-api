package com.example.model

import com.example.utils.UUIDSerializer
import com.example.utils.Validatable
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Pizza(
    @Serializable(with = UUIDSerializer::class) val id: UUID = UUID.randomUUID(),
    @Serializable(with = UUIDSerializer::class) val owner: UUID,
    var name: String,
    var base: PizzaBase,
    override var ingredients: MutableList<Ingredient> = mutableListOf(),
    var side: PizzaSideSerializable,
) : PricedConst, Ingrediented {
    override val price: Double
        get() {
            val ingredientsSum = ingredients.sumOf { it.price }

            return base.price + ingredientsSum + side.price
        }

    constructor(owner: UUID, props: PizzaProps) : this(
        id = UUID.randomUUID(),
        owner = owner,
        name = props.name.orEmpty(),
        ingredients = props.ingredients,
        base = props.base!!,
        side = props.side ?: PizzaSideSerializable(PizzaSide(owner)),
    )
}

@Serializable
data class PizzaProps(
    val name: String? = null,
    val ingredients: MutableList<Ingredient> = mutableListOf(),
    val base: PizzaBase? = null,
    val side: PizzaSideSerializable? = null,
) : Validatable {
    override fun validate() {
        require(!name.isNullOrBlank()) { "Нужно указать название!" }
        require(base != null) { "Нужно указать основу" }
    }
}

@Serializable
data class PizzaRequest(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String,
    val ingredients: MutableList<Ingredient> = mutableListOf(),
    val base: PizzaBase,
    val side: PizzaSideSerializable?,
)

@Serializable
data class PizzaUpdateRequest(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val itemId: UUID,
    val name: String,
    val ingredients: MutableList<Ingredient> = mutableListOf(),
    val base: PizzaBase?,
    val side: PizzaSideSerializable?,
)

@Serializable
data class PizzaReadRequest(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val filterData: PizzaSideFilterProps? = null,
)

@Serializable
data class PizzaDeleteRequest(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val itemId: UUID,
)

@Serializable
data class PizzaSerializable(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val owner: UUID,
    val name: String,
    val ingredients: List<Ingredient>,
    val base: PizzaBase,
    val side: PizzaSideSerializable,
    val price: Double,
) {
    constructor(original: Pizza) : this(
        id = original.id,
        owner = original.owner,
        name = original.name,
        ingredients = original.ingredients,
        base = original.base,
        side = original.side,
        price = original.price
    )
}

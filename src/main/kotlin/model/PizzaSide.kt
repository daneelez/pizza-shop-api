package com.example.model

import com.example.utils.UUIDSerializer
import com.example.utils.Validatable
import kotlinx.serialization.Serializable
import java.util.UUID
import kotlin.math.roundToInt

@Serializable
data class PizzaSide(
    @Serializable(with = UUIDSerializer::class) val id: UUID = UUID.randomUUID(),
    @Serializable(with = UUIDSerializer::class) val owner: UUID,
    internal var name: String,
    internal var ingredients: MutableList<Ingredient> = mutableListOf(),
    internal var notAllowedList: MutableList<PizzaSerializable> = mutableListOf(),
) {
    val price: Double
        get() {
            return (ingredients.sumOf { it.price / 2.5 } * 100).roundToInt() / 100.0
        }

    constructor(owner: UUID, props: PizzaSideProps) : this(
        id = UUID.randomUUID(),
        owner = owner,
        name = props.name.orEmpty(),
        ingredients = props.ingredients,
        notAllowedList = props.notAllowedList,
    )

    constructor(owner: UUID) : this(
        id = UUID.randomUUID(),
        owner = owner,
        name = "Базовый",
        ingredients = mutableListOf(),
        notAllowedList = mutableListOf(),
    )
}

@Serializable
data class PizzaSideProps(
    val name: String? = null,
    val ingredients: MutableList<Ingredient> = mutableListOf(),
    val notAllowedList: MutableList<PizzaSerializable> = mutableListOf(),
) : Validatable {
    override fun validate() {
        require(!name.isNullOrBlank()) { "Нужно указать название!" }
    }
}

@Serializable
data class PizzaSideRequest(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String,
    val ingredients: MutableList<Ingredient> = mutableListOf(),
    val notAllowedList: MutableList<PizzaSerializable> = mutableListOf(),
)

@Serializable
data class PizzaSideUpdateRequest(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val itemId: UUID,
    val name: String,
    val ingredients: MutableList<Ingredient> = mutableListOf(),
    val notAllowedList: MutableList<PizzaSerializable> = mutableListOf(),
)

@Serializable
data class PizzaSideReadRequest(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val filterData: PizzaSideFilterProps? = null,
)

@Serializable
data class PizzaSideDeleteRequest(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val itemId: UUID,
)

@Serializable
data class PizzaSideSerializable(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val owner: UUID,
    val name: String,
    val ingredients: List<Ingredient>,
    val notAllowedList: List<PizzaSerializable>,
    val price: Double,
) {
    constructor(original: PizzaSide) : this(
        id = original.id,
        owner = original.owner,
        name = original.name,
        ingredients = original.ingredients,
        notAllowedList = original.notAllowedList,
        price = original.price
    )
}


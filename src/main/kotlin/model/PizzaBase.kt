package com.example.model

import com.example.model.PizzaSide
import com.example.utils.UUIDSerializer
import com.example.utils.Validatable
import kotlinx.serialization.Serializable
import java.util.UUID
import kotlin.math.roundToInt

@Serializable
data class PizzaBase(
    @Serializable(with = UUIDSerializer::class) val id: UUID = UUID.randomUUID(),
    @Serializable(with = UUIDSerializer::class) val owner: UUID,
    internal var name: String,
    override var price: Double,
) : Priced {
    constructor(owner: UUID, props: PizzaBaseProps) : this(
        id = UUID.randomUUID(),
        owner = owner,
        name = props.name.orEmpty(),
        price = props.price?.let {
            (it * 100).roundToInt() / 100.0
        } ?: 0.0
    )

    constructor(owner: UUID, price: Double) : this(
        id = UUID.randomUUID(),
        owner = owner,
        name = "Классическая",
        price = price,
    )
}

@Serializable
data class PizzaBaseProps(
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
data class PizzaBaseRequest(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String,
    val price: Double,
)

@Serializable
data class PizzaBaseUpdateRequest(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val itemId: UUID,
    val name: String,
    val price: Double,
)

@Serializable
data class PizzaBaseReadRequest(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val filterData: PriceFilterProps? = null,
)

@Serializable
data class PizzaBaseDeleteRequest(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val itemId: UUID,
)
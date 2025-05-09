package com.example.model

import com.example.utils.InstantSerializer
import com.example.utils.UUIDSerializer
import com.example.utils.Validatable
import kotlinx.serialization.Serializable
import java.math.RoundingMode
import java.time.Instant
import java.util.UUID

@Serializable
data class Order(
    @Serializable(with = UUIDSerializer::class) val id: UUID = UUID.randomUUID(),
    val owner: UserResponse,
    val owners: MutableList<UserResponse> = mutableListOf(),
    val pizzas: MutableList<PizzaOrderProps> = mutableListOf(),
    var description: String = "",
    @Serializable(with = InstantSerializer::class) var date: Instant = Instant.now(),
) {
    val price: Double
        get() {
            val basicPrice = pizzas.sumOf { it.price }.toBigDecimal()
            val size = owners.size.toBigDecimal()
            val dividedSum = basicPrice / size

            val diff = basicPrice - (dividedSum * size)

            return (dividedSum + diff).setScale(2, RoundingMode.HALF_UP).toDouble()
        }

    constructor(owner: UserResponse, props: OrderProps) : this(
        id = UUID.randomUUID(),
        owner = owner,
        owners = props.owners,
        pizzas = props.pizzas,
        description = props.description,
        date = props.date,
    )
}

@Serializable
data class OrderProps(
    val owners: MutableList<UserResponse> = mutableListOf(),
    val pizzas: MutableList<PizzaOrderProps> = mutableListOf(),
    val description: String = "",
    @Serializable(with = InstantSerializer::class) val date: Instant = Instant.now(),
)

@Serializable
data class PizzaOrderRequest(
    val slices: MutableList<PizzaSliceRequest> = mutableListOf(),
    val size: PizzaSize,
)

@Serializable
data class PizzaOrderProps(
    val slices: MutableList<PizzaSliceProps> = mutableListOf(),
    val size: PizzaSize,
) : Validatable {
    val price: Double
        get() {
            val slicesSum =
                slices.sumOf {
                    it.side.price +
                            it.base.price +
                            it.ingredients.sumOf { it.price }
                }

            val sizeRatio = when (size) {
                PizzaSize.MIXED -> 1.05
                PizzaSize.SMALL -> 1.0
                PizzaSize.MEDIUM -> 1.3
                PizzaSize.LARGE -> 1.9
            }

            return slicesSum / slices.size * sizeRatio;
        }

    override fun validate() {
        require(slices.size == size.slices) { "Ошибка соответствия размера!" }
    }

    constructor(owner: UserResponse, props: PizzaOrderRequest) : this(
        slices = props.slices.map { PizzaSliceProps(owner, it) }.toMutableList(),
        size = props.size,
    )
}

@Serializable
data class OrderRequest(
    val owner: UserResponse,
    val pizzas: MutableList<PizzaOrderRequest> = mutableListOf(),
    val owners: MutableList<UserResponse> = mutableListOf(),
    val description: String = "",
    @Serializable(with = InstantSerializer::class) val date: Instant = Instant.now(),
)

@Serializable
data class OrderReadRequest(
    val id: UserResponse,
    val filterData: OrderFilterProps? = null,
)

@Serializable
data class OrderUpdateRequest(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val owner: UserResponse,
    val description: String = "",
    @Serializable(with = InstantSerializer::class) val date: Instant = Instant.now(),
)

@Serializable
data class OrderDeleteRequest(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val owner: UserResponse,
)

@Serializable
data class OrderSerializable(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val owner: UserResponse,
    val owners: MutableList<UserResponse>,
    val pizzas: MutableList<PizzaOrderProps>,
    val description: String = "",
    @Serializable(with = InstantSerializer::class) val date: Instant,
    val price: Double,
) {
    constructor(original: Order) : this(
        id = original.id,
        owner = original.owner,
        owners = original.owners,
        pizzas = original.pizzas,
        description = original.description,
        date = original.date,
        price = original.price,
    )
}
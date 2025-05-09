package com.example.model

import kotlinx.serialization.Serializable

@Serializable
enum class PizzaSize(val slices: Int) {
    MIXED(2),
    SMALL(6),
    MEDIUM(8),
    LARGE(12),
}
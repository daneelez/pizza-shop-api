package com.example.model

import com.example.utils.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

interface Dated {
    @Serializable(with = InstantSerializer::class) var date: Instant
}
package com.example.model

import com.example.utils.UUIDSerializer

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class User(
    @Serializable(with = UUIDSerializer::class) val id: UUID = UUID.randomUUID(),
    var name: String,
    var password: String,
)

@Serializable
data class LoginRequest(val name: String, val password: String)

@Serializable
data class RegisterRequest(val name: String, val password: String)

@Serializable
data class LoginResponse(@Serializable(with = UUIDSerializer::class) val id: UUID, val name: String)

@Serializable
data class UserResponse(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String,
)

@Serializable
data class UserReadResponse(
    val users: List<UserResponse>,
)
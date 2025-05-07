package com.example.manager

import java.util.UUID


interface BaseManager<T, P, F> {
    fun create(userId: UUID, values: P): T?
    fun update(userId: UUID, itemId: UUID, values: P)
    fun delete(userId: UUID, itemId: UUID)
    fun read(userId: UUID, filterData: F? = null): List<T>
}
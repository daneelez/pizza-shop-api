package com.example.utils

import java.time.Duration
import java.time.Instant

fun checkDate(date: Instant): Boolean {
    val now = Instant.now()

    return date >= now - Duration.ofSeconds(20)
}
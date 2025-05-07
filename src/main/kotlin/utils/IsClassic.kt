package com.example.utils

fun isClassicBase(name: String?): Boolean {
    if (name == null) return false;
    return name.lowercase().startsWith("classic") || name.lowercase().startsWith("классич")
}
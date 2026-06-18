package com.example.xfitapplication.presentation.util

fun String.parseStrictInt(): Int? {
    val trimmed = trim()
    if (trimmed.isEmpty()) return null
    if (trimmed.contains('.') || trimmed.contains(',')) return null
    return trimmed.toIntOrNull()
}

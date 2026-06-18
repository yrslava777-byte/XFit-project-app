package com.example.xfitapplication.presentation.util

private val INTEGER_PATTERN = Regex("^\\d+$")
private val DECIMAL_PATTERN = Regex("^\\d+([.,]\\d+)?$")

fun String.parseStrictInt(): Int? {
    val trimmed = trim()
    if (trimmed.isEmpty()) return null
    if (!trimmed.matches(INTEGER_PATTERN)) return null
    return trimmed.toIntOrNull()
}

fun String.hasInvalidIntegerCharacters(): Boolean {
    val trimmed = trim()
    return trimmed.isNotEmpty() && !trimmed.matches(INTEGER_PATTERN)
}

fun String.hasInvalidDecimalCharacters(): Boolean {
    val trimmed = trim()
    return trimmed.isNotEmpty() && !trimmed.matches(DECIMAL_PATTERN)
}

fun String.parseStrictDecimal(): Double? {
    val trimmed = trim()
    if (trimmed.isEmpty()) return null
    if (!trimmed.matches(DECIMAL_PATTERN)) return null
    return trimmed.replace(',', '.').toDoubleOrNull()
}

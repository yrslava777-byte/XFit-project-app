package com.example.xfitapplication.domain.validation

object ValidationMessages {
    const val INTEGER_ONLY = "Только целое число"
    const val INVALID_CHARACTER = "Недопустимый символ"
    const val LETTERS_ONLY = "Допустимы только буквы (в том числе русские)"

    fun maxExceeded(max: Int): String =
        "Недопустимое число: можно ввести число меньше или равно $max"

    fun maxExceeded(max: Double): String =
        "Недопустимое число: можно ввести число меньше или равно ${max.toInt()}"
}

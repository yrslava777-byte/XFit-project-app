package com.example.xfitapplication.domain.validation

data class FieldValidationResult(
    val isValid: Boolean,
    val message: String? = null
) {
    companion object {
        fun valid() = FieldValidationResult(isValid = true)

        fun invalid(message: String) = FieldValidationResult(isValid = false, message = message)
    }
}

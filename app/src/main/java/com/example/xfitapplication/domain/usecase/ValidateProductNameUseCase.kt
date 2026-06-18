package com.example.xfitapplication.domain.usecase

import com.example.xfitapplication.domain.validation.FieldValidationResult
import com.example.xfitapplication.domain.validation.InputLimits
import com.example.xfitapplication.domain.validation.ValidationMessages

class ValidateProductNameUseCase {

    fun execute(name: String): FieldValidationResult {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) {
            return FieldValidationResult.invalid("Введите название продукта")
        }
        if (trimmed.length < InputLimits.MIN_PRODUCT_NAME_LENGTH) {
            return FieldValidationResult.invalid("Название слишком короткое")
        }
        if (trimmed.length > InputLimits.MAX_PRODUCT_NAME_LENGTH) {
            return FieldValidationResult.invalid(
                ValidationMessages.maxExceeded(InputLimits.MAX_PRODUCT_NAME_LENGTH)
            )
        }
        if (!trimmed.any { it.isLetter() }) {
            return FieldValidationResult.invalid(ValidationMessages.LETTERS_ONLY)
        }
        if (!trimmed.matches(LETTERS_SPACES_HYPHEN)) {
            return FieldValidationResult.invalid(ValidationMessages.LETTERS_ONLY)
        }
        return FieldValidationResult.valid()
    }

    companion object {
        private val LETTERS_SPACES_HYPHEN = Regex("^[\\p{L}\\s\\-]+$")
    }
}

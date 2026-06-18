package com.example.xfitapplication.domain.usecase

import com.example.xfitapplication.domain.validation.FieldValidationResult
import com.example.xfitapplication.domain.validation.InputLimits
import com.example.xfitapplication.domain.validation.ValidationMessages

class ValidatePortionWeightUseCase {

    fun execute(weightGrams: Double): FieldValidationResult = when {
        weightGrams <= 0 -> FieldValidationResult.invalid("Укажите вес порции")
        weightGrams > InputLimits.MAX_PORTION_WEIGHT_GRAMS ->
            FieldValidationResult.invalid(ValidationMessages.maxExceeded(InputLimits.MAX_PORTION_WEIGHT_GRAMS))
        else -> FieldValidationResult.valid()
    }
}

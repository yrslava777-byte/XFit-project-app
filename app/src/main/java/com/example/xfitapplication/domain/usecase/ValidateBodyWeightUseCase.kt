package com.example.xfitapplication.domain.usecase

import com.example.xfitapplication.domain.validation.FieldValidationResult
import com.example.xfitapplication.domain.validation.InputLimits
import com.example.xfitapplication.domain.validation.ValidationMessages

class ValidateBodyWeightUseCase {

    fun execute(weightKg: Int): FieldValidationResult = when {
        weightKg <= 0 -> FieldValidationResult.invalid("Укажите вес больше 0")
        weightKg > InputLimits.MAX_BODY_WEIGHT_KG ->
            FieldValidationResult.invalid(ValidationMessages.maxExceeded(InputLimits.MAX_BODY_WEIGHT_KG))
        else -> FieldValidationResult.valid()
    }
}

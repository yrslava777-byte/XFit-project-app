package com.example.xfitapplication.domain.usecase

import com.example.xfitapplication.domain.validation.FieldValidationResult
import com.example.xfitapplication.domain.validation.InputLimits
import com.example.xfitapplication.domain.validation.ValidationMessages

class ValidateAgeUseCase {

    fun execute(ageYears: Int): FieldValidationResult = when {
        ageYears <= 0 -> FieldValidationResult.invalid("Укажите возраст больше 0")
        ageYears > InputLimits.MAX_AGE_YEARS ->
            FieldValidationResult.invalid(ValidationMessages.maxExceeded(InputLimits.MAX_AGE_YEARS))
        else -> FieldValidationResult.valid()
    }
}

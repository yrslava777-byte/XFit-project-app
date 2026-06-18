package com.example.xfitapplication.domain.usecase

import com.example.xfitapplication.domain.validation.FieldValidationResult
import com.example.xfitapplication.domain.validation.InputLimits
import com.example.xfitapplication.domain.validation.ValidationMessages

class ValidateCaloriesUseCase {

    fun execute(calories: Int): FieldValidationResult = when {
        calories <= 0 -> FieldValidationResult.invalid("Калории должны быть целым числом больше 0")
        calories > InputLimits.MAX_DAILY_CALORIES ->
            FieldValidationResult.invalid(ValidationMessages.maxExceeded(InputLimits.MAX_DAILY_CALORIES))
        else -> FieldValidationResult.valid()
    }
}

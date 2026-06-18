package com.example.xfitapplication.domain.usecase

import com.example.xfitapplication.domain.validation.FieldValidationResult
import com.example.xfitapplication.domain.validation.InputLimits
import com.example.xfitapplication.domain.validation.ValidationMessages

class ValidateNutritionPer100UseCase {

    enum class Nutrient(val label: String) {
        CALORIES("Калории"),
        PROTEIN("Белки"),
        FAT("Жиры"),
        CARBS("Углеводы")
    }

    fun execute(nutrient: Nutrient, value: Double): FieldValidationResult = when (nutrient) {
        Nutrient.CALORIES -> when {
            value <= 0 -> FieldValidationResult.invalid("Калории должны быть больше 0")
            value > InputLimits.MAX_CALORIES_PER_100G ->
                FieldValidationResult.invalid(ValidationMessages.maxExceeded(InputLimits.MAX_CALORIES_PER_100G))
            else -> FieldValidationResult.valid()
        }
        Nutrient.PROTEIN, Nutrient.FAT, Nutrient.CARBS -> when {
            value < 0 -> FieldValidationResult.invalid("${nutrient.label} не могут быть отрицательными")
            value > InputLimits.MAX_MACRO_PER_100G ->
                FieldValidationResult.invalid(ValidationMessages.maxExceeded(InputLimits.MAX_MACRO_PER_100G.toInt()))
            else -> FieldValidationResult.valid()
        }
    }
}

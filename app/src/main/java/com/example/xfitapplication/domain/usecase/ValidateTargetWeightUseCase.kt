package com.example.xfitapplication.domain.usecase

import com.example.xfitapplication.domain.validation.FieldValidationResult
import com.example.xfitapplication.domain.validation.InputLimits
import com.example.xfitapplication.domain.validation.ValidationMessages

class ValidateTargetWeightUseCase {

    fun execute(heightCm: Double, targetWeightKg: Double): FieldValidationResult {
        if (targetWeightKg > InputLimits.MAX_BODY_WEIGHT_KG) {
            return FieldValidationResult.invalid(
                ValidationMessages.maxExceeded(InputLimits.MAX_BODY_WEIGHT_KG)
            )
        }
        if (targetWeightKg < MIN_WEIGHT_KG) {
            return FieldValidationResult.invalid("Целевой вес не может быть меньше $MIN_WEIGHT_KG кг")
        }
        val heightM = heightCm / 100.0
        val bmi = targetWeightKg / (heightM * heightM)
        if (bmi < MIN_BMI) {
            return FieldValidationResult.invalid(
                "Целевой вес слишком низкий для вашего роста (ИМТ < $MIN_BMI)"
            )
        }
        return FieldValidationResult.valid()
    }

    companion object {
        private const val MIN_WEIGHT_KG = 40.0
        private const val MIN_BMI = 18.5
    }
}

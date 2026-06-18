package com.example.xfitapplication.domain.usecase

class ValidateTargetWeightUseCase {

    data class Result(val isValid: Boolean, val message: String? = null)

    fun execute(heightCm: Double, targetWeightKg: Double): Result {
        if (targetWeightKg < MIN_WEIGHT_KG) {
            return Result(false, "Целевой вес не может быть меньше $MIN_WEIGHT_KG кг")
        }
        val heightM = heightCm / 100.0
        val bmi = targetWeightKg / (heightM * heightM)
        if (bmi < MIN_BMI) {
            return Result(false, "Целевой вес слишком низкий для вашего роста (ИМТ < $MIN_BMI)")
        }
        return Result(true)
    }

    companion object {
        private const val MIN_WEIGHT_KG = 40.0
        private const val MIN_BMI = 18.5
    }
}

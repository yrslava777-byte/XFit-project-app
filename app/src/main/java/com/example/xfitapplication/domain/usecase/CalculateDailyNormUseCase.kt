package com.example.xfitapplication.domain.usecase

import com.example.xfitapplication.domain.model.User
import kotlin.math.abs
import kotlin.math.roundToInt

class CalculateDailyNormUseCase {

    fun execute(
        heightCm: Double,
        weightKg: Double,
        targetWeightKg: Double,
        ageYears: Int,
        gender: String,
        activityLevel: Int
    ): User {
        val bmr = if (gender == "male") {
            (10 * weightKg) + (6.25 * heightCm) - (5 * ageYears) + 5
        } else {
            (10 * weightKg) + (6.25 * heightCm) - (5 * ageYears) - 161
        }

        val activityMultiplier = when (activityLevel) {
            1 -> 1.2
            2 -> 1.375
            3 -> 1.55
            4 -> 1.725
            5 -> 1.9
            else -> 1.2
        }

        val tdee = bmr * activityMultiplier
        val weightDiff = weightKg - targetWeightKg

        val dailyCalories = when {
            weightDiff > 0.5 -> {
                val deficit = (weightDiff * 25).coerceAtMost(750.0)
                (tdee - deficit).coerceAtLeast(minCalories(gender))
            }
            weightDiff < -0.5 -> {
                val surplus = (abs(weightDiff) * 20).coerceAtMost(500.0)
                tdee + surplus
            }
            else -> tdee
        }.roundToInt().toDouble()

        val dailyProtein = ((dailyCalories * 0.30) / 4).roundToInt().toDouble()
        val dailyFat = ((dailyCalories * 0.25) / 9).roundToInt().toDouble()
        val dailyCarbs = ((dailyCalories * 0.45) / 4).roundToInt().toDouble()

        return User(
            heightCm = heightCm,
            weightKg = weightKg,
            ageYears = ageYears,
            gender = gender,
            activityLevel = activityLevel,
            targetWeightKg = targetWeightKg,
            dailyCalories = dailyCalories,
            dailyProtein = dailyProtein,
            dailyFat = dailyFat,
            dailyCarbs = dailyCarbs
        )
    }

    private fun minCalories(gender: String): Double =
        if (gender == "male") MIN_CALORIES_MALE else MIN_CALORIES_FEMALE

    companion object {
        private const val MIN_CALORIES_MALE = 1500.0
        private const val MIN_CALORIES_FEMALE = 1200.0
    }
}

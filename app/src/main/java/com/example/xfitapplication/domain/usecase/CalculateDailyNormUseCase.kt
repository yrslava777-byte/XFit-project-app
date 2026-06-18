package com.example.xfitapplication.domain.usecase

import com.example.xfitapplication.domain.model.User

class CalculateDailyNormUseCase {
    fun execute(
        heightCm: Double,
        weightKg: Double,
        ageYears: Int,
        gender: String,
        activityLevel: Int
    ): User {
        // Формула Миффлина-Сан Жеора
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

        val dailyCalories = bmr * activityMultiplier

        // БЖУ: 30% белки, 25% жиры, 45% углеводы
        val dailyProtein = (dailyCalories * 0.30) / 4
        val dailyFat = (dailyCalories * 0.25) / 9
        val dailyCarbs = (dailyCalories * 0.45) / 4

        return User(
            heightCm = heightCm,
            weightKg = weightKg,
            ageYears = ageYears,
            gender = gender,
            activityLevel = activityLevel,
            targetWeightKg = null,
            dailyCalories = dailyCalories,
            dailyProtein = dailyProtein,
            dailyFat = dailyFat,
            dailyCarbs = dailyCarbs
        )
    }
}
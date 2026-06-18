package com.example.xfitapplication.domain.model

data class User(val id: Int = 1,
                val heightCm: Double,
                val weightKg: Double,
                val ageYears: Int,
                val gender: String,
                val activityLevel: Int,
                val targetWeightKg: Double?,
                val dailyCalories: Double,
                val dailyProtein: Double,
                val dailyFat: Double,
                val dailyCarbs: Double
)

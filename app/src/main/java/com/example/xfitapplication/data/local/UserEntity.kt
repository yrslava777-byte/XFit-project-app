package com.example.xfitapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int = 1,
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
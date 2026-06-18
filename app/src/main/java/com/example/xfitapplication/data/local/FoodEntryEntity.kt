package com.example.xfitapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_entries")
data class FoodEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val productId: Int,
    val productName: String,
    val weightGrams: Double,
    val caloriesTotal: Double,
    val proteinTotal: Double,
    val fatTotal: Double,
    val carbsTotal: Double,
    val mealType: String,
    val mealDate: String
)

package com.example.xfitapplication.domain.model

data class FoodEntry(val id: Int,
                     val productId: Int,
                     val productName: String,
                     val weightGrams: Double,
                     val caloriesTotal: Double,
                     val proteinTotal: Double,
                     val fatTotal: Double,
                     val carbsTotal: Double,
                     val mealType: String,
                     val mealDate: String)

package com.example.xfitapplication.domain.usecase

import com.example.xfitapplication.domain.model.FoodEntry
import com.example.xfitapplication.domain.model.Product

class AddFoodEntryUseCase {
    fun execute(
        product: Product,
        weightGrams: Double,
        mealType: String,
        mealDate: String
    ): FoodEntry {
        val factor = weightGrams / 100.0
        return FoodEntry(
            id = 0,
            productId = product.id,
            productName = product.name,
            weightGrams = weightGrams,
            caloriesTotal = product.caloriesPer100g * factor,
            proteinTotal = product.proteinPer100g * factor,
            fatTotal = product.fatPer100g * factor,
            carbsTotal = product.carbsPer100g * factor,
            mealType = mealType,
            mealDate = mealDate
        )
    }
}

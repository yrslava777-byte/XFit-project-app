package com.example.xfitapplication.domain.usecase

import com.example.xfitapplication.domain.model.Product

class AddProductUseCase {

    data class Form(
        val name: String,
        val caloriesPer100g: Double,
        val proteinPer100g: Double,
        val fatPer100g: Double,
        val carbsPer100g: Double
    )

    data class Result(
        val product: Product?,
        val errors: List<String>
    )

    private val validateName = ValidateProductNameUseCase()
    private val validateNutrition = ValidateNutritionPer100UseCase()

    fun validate(form: Form): List<String> = buildList {
        validateName.execute(form.name).message?.let { add(it) }
        validateNutrition.execute(ValidateNutritionPer100UseCase.Nutrient.CALORIES, form.caloriesPer100g)
            .message?.let { add(it) }
        validateNutrition.execute(ValidateNutritionPer100UseCase.Nutrient.PROTEIN, form.proteinPer100g)
            .message?.let { add(it) }
        validateNutrition.execute(ValidateNutritionPer100UseCase.Nutrient.FAT, form.fatPer100g)
            .message?.let { add(it) }
        validateNutrition.execute(ValidateNutritionPer100UseCase.Nutrient.CARBS, form.carbsPer100g)
            .message?.let { add(it) }
    }

    fun buildProduct(form: Form): Product = Product(
        id = 0,
        name = form.name.trim(),
        caloriesPer100g = form.caloriesPer100g,
        proteinPer100g = form.proteinPer100g,
        fatPer100g = form.fatPer100g,
        carbsPer100g = form.carbsPer100g,
        isCustom = true
    )
}

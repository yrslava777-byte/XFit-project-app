package com.example.xfitapplication.domain.model

data class Product(val id: Int,
                   val name: String,
                   val caloriesPer100g: Double,
                   val proteinPer100g: Double,
                   val fatPer100g: Double,
                   val carbsPer100g: Double,
                   val isCustom: Boolean = false)

package com.example.xfitapplication.data.mapper

import com.example.xfitapplication.data.local.FoodEntryEntity
import com.example.xfitapplication.data.local.ProductEntity
import com.example.xfitapplication.data.local.UserEntity
import com.example.xfitapplication.domain.model.FoodEntry
import com.example.xfitapplication.domain.model.Product
import com.example.xfitapplication.domain.model.User

fun UserEntity.toDomain() = User(
    id = id,
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

fun User.toEntity() = UserEntity(
    id = id,
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

fun ProductEntity.toDomain() = Product(
    id = id,
    name = name,
    caloriesPer100g = caloriesPer100g,
    proteinPer100g = proteinPer100g,
    fatPer100g = fatPer100g,
    carbsPer100g = carbsPer100g,
    isCustom = isCustom
)

fun Product.toEntity() = ProductEntity(
    id = id,
    name = name,
    caloriesPer100g = caloriesPer100g,
    proteinPer100g = proteinPer100g,
    fatPer100g = fatPer100g,
    carbsPer100g = carbsPer100g,
    isCustom = isCustom
)

fun FoodEntryEntity.toDomain() = FoodEntry(
    id = id,
    productId = productId,
    productName = productName,
    weightGrams = weightGrams,
    caloriesTotal = caloriesTotal,
    proteinTotal = proteinTotal,
    fatTotal = fatTotal,
    carbsTotal = carbsTotal,
    mealType = mealType,
    mealDate = mealDate
)

fun FoodEntry.toEntity() = FoodEntryEntity(
    id = id,
    productId = productId,
    productName = productName,
    weightGrams = weightGrams,
    caloriesTotal = caloriesTotal,
    proteinTotal = proteinTotal,
    fatTotal = fatTotal,
    carbsTotal = carbsTotal,
    mealType = mealType,
    mealDate = mealDate
)

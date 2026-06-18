package com.example.xfitapplication.presentation.screens

import java.io.Serializable

data class DiaryEntry(
    val productName: String,
    val weight: Double,
    val calories: Double,
    val protein: Double,
    val fat: Double,
    val carbs: Double
) : Serializable
package com.example.xfitapplication.domain.usecase

import com.example.xfitapplication.domain.model.FoodEntry
import com.example.xfitapplication.domain.model.User

class GetDailyProgressUseCase {

    data class DailyProgress(
        val normCalories: Double,
        val normProtein: Double,
        val normFat: Double,
        val normCarbs: Double,
        val consumedCalories: Double,
        val consumedProtein: Double,
        val consumedFat: Double,
        val consumedCarbs: Double,
        val breakfastCalories: Double,
        val lunchCalories: Double,
        val dinnerCalories: Double,
        val remainingCalories: Double
    )

    fun execute(user: User?, entries: List<FoodEntry>): DailyProgress {
        val normCal = user?.dailyCalories ?: 2000.0
        val normProt = user?.dailyProtein ?: 120.0
        val normFat = user?.dailyFat ?: 60.0
        val normCarb = user?.dailyCarbs ?: 250.0

        val consumedCal = entries.sumOf { it.caloriesTotal }
        val consumedProt = entries.sumOf { it.proteinTotal }
        val consumedFat = entries.sumOf { it.fatTotal }
        val consumedCarb = entries.sumOf { it.carbsTotal }

        return DailyProgress(
            normCalories = normCal,
            normProtein = normProt,
            normFat = normFat,
            normCarbs = normCarb,
            consumedCalories = consumedCal,
            consumedProtein = consumedProt,
            consumedFat = consumedFat,
            consumedCarbs = consumedCarb,
            breakfastCalories = entries.filter { it.mealType == "breakfast" }.sumOf { it.caloriesTotal },
            lunchCalories = entries.filter { it.mealType == "lunch" }.sumOf { it.caloriesTotal },
            dinnerCalories = entries.filter { it.mealType == "dinner" }.sumOf { it.caloriesTotal },
            remainingCalories = normCal - consumedCal
        )
    }
}

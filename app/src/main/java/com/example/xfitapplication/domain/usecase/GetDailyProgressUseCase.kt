package com.example.xfitapplication.domain.usecase

import com.example.xfitapplication.domain.model.FoodEntry
import com.example.xfitapplication.domain.model.User
import kotlin.math.roundToInt

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
        val breakfastTargetCalories: Int,
        val lunchTargetCalories: Int,
        val dinnerTargetCalories: Int,
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

        val mealTargets = mealTargetsFor(normCal)

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
            breakfastTargetCalories = mealTargets.first,
            lunchTargetCalories = mealTargets.second,
            dinnerTargetCalories = mealTargets.third,
            remainingCalories = normCal - consumedCal
        )
    }

    private fun mealTargetsFor(dailyCalories: Double): Triple<Int, Int, Int> {
        val breakfast = (dailyCalories * BREAKFAST_RATIO).roundToInt()
        val lunch = (dailyCalories * LUNCH_RATIO).roundToInt()
        val dinner = (dailyCalories - breakfast - lunch).roundToInt().coerceAtLeast(0)
        return Triple(breakfast, lunch, dinner)
    }

    companion object {
        private const val BREAKFAST_RATIO = 500.0 / 1750.0
        private const val LUNCH_RATIO = 700.0 / 1750.0
    }
}

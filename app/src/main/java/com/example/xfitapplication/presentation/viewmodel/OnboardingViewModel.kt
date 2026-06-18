package com.example.xfitapplication.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.xfitapplication.data.local.AppDatabase
import com.example.xfitapplication.data.repository.FoodRepository
import com.example.xfitapplication.domain.model.User
import com.example.xfitapplication.domain.usecase.CalculateDailyNormUseCase
import kotlinx.coroutines.launch

class OnboardingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FoodRepository
    private val useCase = CalculateDailyNormUseCase()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = FoodRepository(db.userDao(), db.productDao(), db.foodEntryDao())
    }

    fun calculateAndSave(
        height: Double,
        weight: Double,
        age: Int,
        gender: String,
        activityLevel: Int,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val user = useCase.execute(height, weight, age, gender, activityLevel)
            repository.insertUser(
                com.example.xfitapplication.data.local.UserEntity(
                    id = user.id,
                    heightCm = user.heightCm,
                    weightKg = user.weightKg,
                    ageYears = user.ageYears,
                    gender = user.gender,
                    activityLevel = user.activityLevel,
                    targetWeightKg = user.targetWeightKg,
                    dailyCalories = user.dailyCalories,
                    dailyProtein = user.dailyProtein,
                    dailyFat = user.dailyFat,
                    dailyCarbs = user.dailyCarbs
                )
            )
            onSuccess()
        }
    }
}S
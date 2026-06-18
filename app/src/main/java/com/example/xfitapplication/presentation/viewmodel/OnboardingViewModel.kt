package com.example.xfitapplication.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.xfitapplication.domain.repository.FoodRepository
import com.example.xfitapplication.domain.usecase.CalculateDailyNormUseCase
import com.example.xfitapplication.domain.usecase.ValidateTargetWeightUseCase
import com.example.xfitapplication.presentation.RepositoryProvider
import com.example.xfitapplication.presentation.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OnboardingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FoodRepository = RepositoryProvider.getRepository(application)
    private val calculateNorm = CalculateDailyNormUseCase()
    private val validateWeight = ValidateTargetWeightUseCase()

    private val _navigateToDashboard = MutableLiveData<Boolean>()
    val navigateToDashboard: LiveData<Boolean> = _navigateToDashboard

    private val _error = MutableLiveData<Event<String>>()
    val error: LiveData<Event<String>> = _error

    init {
        viewModelScope.launch {
            repository.seedProductsIfEmpty()
            if (repository.getUserOnce() != null) {
                _navigateToDashboard.postValue(true)
            }
        }
    }

    fun calculateAndSave(
        height: Int,
        weight: Int,
        targetWeight: Int,
        age: Int,
        gender: String,
        activityLevel: Int,
        onSuccess: () -> Unit
    ) {
        val validation = validateWeight.execute(height.toDouble(), targetWeight.toDouble())
        if (!validation.isValid) {
            _error.value = Event(validation.message ?: "Некорректный целевой вес")
            return
        }

        viewModelScope.launch {
            val user = calculateNorm.execute(
                heightCm = height.toDouble(),
                weightKg = weight.toDouble(),
                targetWeightKg = targetWeight.toDouble(),
                ageYears = age,
                gender = gender,
                activityLevel = activityLevel
            )
            repository.saveUser(user)
            withContext(Dispatchers.Main) { onSuccess() }
        }
    }
}

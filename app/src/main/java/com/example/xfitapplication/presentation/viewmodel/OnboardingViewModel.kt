package com.example.xfitapplication.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.xfitapplication.domain.repository.FoodRepository
import com.example.xfitapplication.domain.usecase.CalculateDailyNormUseCase
import com.example.xfitapplication.domain.usecase.ValidateAgeUseCase
import com.example.xfitapplication.domain.usecase.ValidateBodyWeightUseCase
import com.example.xfitapplication.domain.usecase.ValidateTargetWeightUseCase
import com.example.xfitapplication.presentation.RepositoryProvider
import com.example.xfitapplication.presentation.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OnboardingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FoodRepository = RepositoryProvider.getRepository(application)
    private val calculateNorm = CalculateDailyNormUseCase()
    private val validateBodyWeight = ValidateBodyWeightUseCase()
    private val validateAge = ValidateAgeUseCase()
    private val validateTargetWeight = ValidateTargetWeightUseCase()

    private val _navigateToDashboard = MutableLiveData<Boolean>()
    val navigateToDashboard: LiveData<Boolean> = _navigateToDashboard

    private val _error = MutableLiveData<Event<String>>()
    val error: LiveData<Event<String>> = _error

    init {
        viewModelScope.launch {
            try {
                repository.seedProductsIfEmpty()
                if (repository.getUserOnce() != null) {
                    _navigateToDashboard.postValue(true)
                }
            } catch (_: Exception) {
                // Пользователь останется на онбординге и сможет повторить ввод
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
        val errors = buildList {
            validateBodyWeight.execute(weight).message?.let { add(it) }
            validateBodyWeight.execute(targetWeight).message?.let { add(it) }
            validateAge.execute(age).message?.let { add(it) }
            val targetValidation = validateTargetWeight.execute(height.toDouble(), targetWeight.toDouble())
            if (!targetValidation.isValid) {
                add(targetValidation.message ?: "Некорректный целевой вес")
            }
        }
        if (errors.isNotEmpty()) {
            _error.value = Event(errors.joinToString("\n"))
            return
        }

        viewModelScope.launch {
            try {
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
            } catch (_: Exception) {
                _error.postValue(Event("Не удалось сохранить данные. Попробуйте ещё раз."))
            }
        }
    }
}

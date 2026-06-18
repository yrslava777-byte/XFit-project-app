package com.example.xfitapplication.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.xfitapplication.domain.model.User
import com.example.xfitapplication.domain.repository.FoodRepository
import com.example.xfitapplication.domain.usecase.CalculateDailyNormUseCase
import com.example.xfitapplication.presentation.RepositoryProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OnboardingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FoodRepository = RepositoryProvider.getRepository(application)
    private val calculateNorm = CalculateDailyNormUseCase()

    private val _navigateToDashboard = MutableLiveData<Boolean>()
    val navigateToDashboard: LiveData<Boolean> = _navigateToDashboard

    init {
        viewModelScope.launch {
            repository.seedProductsIfEmpty()
            if (repository.getUserOnce() != null) {
                _navigateToDashboard.postValue(true)
            }
        }
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
            val user = calculateNorm.execute(height, weight, age, gender, activityLevel)
            repository.saveUser(user)
            withContext(Dispatchers.Main) { onSuccess() }
        }
    }
}

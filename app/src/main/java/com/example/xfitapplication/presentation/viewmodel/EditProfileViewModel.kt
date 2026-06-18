package com.example.xfitapplication.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.xfitapplication.domain.model.User
import com.example.xfitapplication.domain.repository.FoodRepository
import com.example.xfitapplication.domain.usecase.ValidateTargetWeightUseCase
import com.example.xfitapplication.presentation.RepositoryProvider
import com.example.xfitapplication.presentation.util.Event
import kotlinx.coroutines.launch

class EditProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FoodRepository = RepositoryProvider.getRepository(application)
    private val validateWeight = ValidateTargetWeightUseCase()

    val user: LiveData<User?> = repository.getUser().asLiveData()

    private val _saveResult = MutableLiveData<Event<SaveResult>>()
    val saveResult: LiveData<Event<SaveResult>> = _saveResult

    sealed class SaveResult {
        data object Success : SaveResult()
        data class Error(val message: String) : SaveResult()
    }

    data class ProfileForm(
        val height: Int,
        val weight: Int,
        val age: Int,
        val targetWeight: Int,
        val calories: Int,
        val protein: Int,
        val fat: Int,
        val carbs: Int
    )

    fun save(form: ProfileForm) {
        validateForm(form)?.let { message ->
            _saveResult.value = Event(SaveResult.Error(message))
            return
        }

        viewModelScope.launch {
            val existing = repository.getUserOnce()
            val user = User(
                id = existing?.id ?: 1,
                heightCm = form.height.toDouble(),
                weightKg = form.weight.toDouble(),
                ageYears = form.age,
                gender = existing?.gender ?: "female",
                activityLevel = existing?.activityLevel ?: 2,
                targetWeightKg = form.targetWeight.toDouble(),
                dailyCalories = form.calories.toDouble(),
                dailyProtein = form.protein.toDouble(),
                dailyFat = form.fat.toDouble(),
                dailyCarbs = form.carbs.toDouble()
            )
            repository.saveUser(user)
            _saveResult.postValue(Event(SaveResult.Success))
        }
    }

    private fun validateForm(form: ProfileForm): String? = when {
        form.height <= 0 || form.height > 250 -> "Укажите корректный рост (см), целое число"
        form.weight <= 0 || form.weight > 300 -> "Укажите корректный вес (кг), целое число"
        form.age <= 0 || form.age > 120 -> "Укажите корректный возраст, целое число"
        form.targetWeight <= 0 -> "Укажите корректный целевой вес (кг), целое число"
        form.calories <= 0 -> "Калории должны быть целым числом больше 0"
        form.protein < 0 -> "Белки должны быть целым числом"
        form.fat < 0 -> "Жиры должны быть целым числом"
        form.carbs < 0 -> "Углеводы должны быть целым числом"
        else -> {
            val validation = validateWeight.execute(form.height.toDouble(), form.targetWeight.toDouble())
            if (!validation.isValid) validation.message else null
        }
    }
}

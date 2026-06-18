package com.example.xfitapplication.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.xfitapplication.domain.model.User
import com.example.xfitapplication.domain.repository.FoodRepository
import com.example.xfitapplication.domain.usecase.ValidateAgeUseCase
import com.example.xfitapplication.domain.usecase.ValidateBodyWeightUseCase
import com.example.xfitapplication.domain.usecase.ValidateCaloriesUseCase
import com.example.xfitapplication.domain.usecase.ValidateMacroUseCase
import com.example.xfitapplication.domain.usecase.ValidateTargetWeightUseCase
import com.example.xfitapplication.presentation.RepositoryProvider
import com.example.xfitapplication.presentation.util.Event
import kotlinx.coroutines.launch

class EditProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FoodRepository = RepositoryProvider.getRepository(application)
    private val validateBodyWeight = ValidateBodyWeightUseCase()
    private val validateAge = ValidateAgeUseCase()
    private val validateCalories = ValidateCaloriesUseCase()
    private val validateMacro = ValidateMacroUseCase()
    private val validateTargetWeight = ValidateTargetWeightUseCase()

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
            try {
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
            } catch (_: Exception) {
                _saveResult.postValue(Event(SaveResult.Error("Не удалось сохранить изменения")))
            }
        }
    }

    private fun validateForm(form: ProfileForm): String? {
        val errors = buildList {
            if (form.height <= 0 || form.height > 250) {
                add("Укажите корректный рост (см), целое число")
            }
            validateBodyWeight.execute(form.weight).message?.let { add(it) }
            validateAge.execute(form.age).message?.let { add(it) }
            validateBodyWeight.execute(form.targetWeight).message?.let { add(it) }
            validateCalories.execute(form.calories).message?.let { add(it) }
            validateMacro.execute(ValidateMacroUseCase.Macro.PROTEIN, form.protein).message?.let { add(it) }
            validateMacro.execute(ValidateMacroUseCase.Macro.FAT, form.fat).message?.let { add(it) }
            validateMacro.execute(ValidateMacroUseCase.Macro.CARBS, form.carbs).message?.let { add(it) }
            val targetValidation = validateTargetWeight.execute(
                form.height.toDouble(),
                form.targetWeight.toDouble()
            )
            if (!targetValidation.isValid) {
                add(targetValidation.message ?: "Некорректный целевой вес")
            }
        }
        return errors.takeIf { it.isNotEmpty() }?.joinToString("\n")
    }
}

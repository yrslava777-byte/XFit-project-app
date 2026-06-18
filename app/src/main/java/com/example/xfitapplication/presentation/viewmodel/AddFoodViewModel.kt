package com.example.xfitapplication.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.xfitapplication.domain.model.FoodEntry
import com.example.xfitapplication.domain.model.Product
import com.example.xfitapplication.domain.repository.FoodRepository
import com.example.xfitapplication.domain.usecase.AddFoodEntryUseCase
import com.example.xfitapplication.domain.usecase.ValidatePortionWeightUseCase
import com.example.xfitapplication.presentation.RepositoryProvider
import com.example.xfitapplication.presentation.util.DateUtils
import com.example.xfitapplication.presentation.util.Event
import kotlinx.coroutines.launch

class AddFoodViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FoodRepository = RepositoryProvider.getRepository(application)
    private val addFoodEntry = AddFoodEntryUseCase()
    private val validatePortionWeight = ValidatePortionWeightUseCase()

    private var product: Product? = null

    private val _error = MutableLiveData<Event<String>>()
    val error: LiveData<Event<String>> = _error

    fun setProduct(
        id: Int,
        name: String,
        kcal: Double,
        prot: Double,
        fat: Double,
        carb: Double
    ) {
        product = Product(id, name, kcal, prot, fat, carb)
    }

    fun addFood(weightGrams: Double, mealType: String, onSuccess: () -> Unit) {
        val validation = validatePortionWeight.execute(weightGrams)
        if (!validation.isValid) {
            _error.value = Event(validation.message ?: "Некорректный вес порции")
            return
        }

        val p = product ?: return
        viewModelScope.launch {
            try {
                val entry = addFoodEntry.execute(p, weightGrams, mealType, DateUtils.todayDb())
                repository.addEntry(entry)
                onSuccess()
            } catch (_: Exception) {
                _error.postValue(Event("Не удалось добавить продукт в дневник"))
            }
        }
    }
}

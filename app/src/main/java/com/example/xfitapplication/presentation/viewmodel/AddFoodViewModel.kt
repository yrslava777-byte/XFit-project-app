package com.example.xfitapplication.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.xfitapplication.domain.model.FoodEntry
import com.example.xfitapplication.domain.model.Product
import com.example.xfitapplication.domain.repository.FoodRepository
import com.example.xfitapplication.domain.usecase.AddFoodEntryUseCase
import com.example.xfitapplication.presentation.RepositoryProvider
import com.example.xfitapplication.presentation.util.DateUtils
import kotlinx.coroutines.launch

class AddFoodViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FoodRepository = RepositoryProvider.getRepository(application)
    private val addFoodEntry = AddFoodEntryUseCase()

    private var product: Product? = null

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
        val p = product ?: return
        viewModelScope.launch {
            val entry = addFoodEntry.execute(p, weightGrams, mealType, DateUtils.todayDb())
            repository.addEntry(entry)
            onSuccess()
        }
    }
}

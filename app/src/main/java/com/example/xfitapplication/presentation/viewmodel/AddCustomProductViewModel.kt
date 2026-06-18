package com.example.xfitapplication.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.xfitapplication.domain.model.Product
import com.example.xfitapplication.domain.repository.FoodRepository
import com.example.xfitapplication.domain.usecase.AddProductUseCase
import com.example.xfitapplication.presentation.RepositoryProvider
import com.example.xfitapplication.presentation.util.Event
import kotlinx.coroutines.launch

class AddCustomProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FoodRepository = RepositoryProvider.getRepository(application)
    private val addProductUseCase = AddProductUseCase()

    private val _saveResult = MutableLiveData<Event<SaveResult>>()
    val saveResult: LiveData<Event<SaveResult>> = _saveResult

    sealed class SaveResult {
        data class Success(val product: Product) : SaveResult()
        data class Error(val message: String) : SaveResult()
    }

    fun save(form: AddProductUseCase.Form) {
        val errors = addProductUseCase.validate(form)
        if (errors.isNotEmpty()) {
            _saveResult.value = Event(SaveResult.Error(errors.joinToString("\n")))
            return
        }

        viewModelScope.launch {
            try {
                val product = repository.addProduct(addProductUseCase.buildProduct(form))
                _saveResult.postValue(Event(SaveResult.Success(product)))
            } catch (_: Exception) {
                _saveResult.postValue(Event(SaveResult.Error("Не удалось сохранить продукт")))
            }
        }
    }
}

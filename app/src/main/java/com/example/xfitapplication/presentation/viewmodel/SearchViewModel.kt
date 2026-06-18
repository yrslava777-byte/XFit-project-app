package com.example.xfitapplication.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.xfitapplication.domain.model.Product
import com.example.xfitapplication.domain.repository.FoodRepository
import com.example.xfitapplication.presentation.RepositoryProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FoodRepository = RepositoryProvider.getRepository(application)
    private val query = MutableStateFlow("")

    val products: LiveData<List<Product>> = query
        .flatMapLatest { q ->
            if (q.isBlank()) repository.getAllProducts()
            else repository.searchProducts(q)
        }
        .asLiveData()

    fun setQuery(text: String) {
        query.value = text
    }
}

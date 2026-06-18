package com.example.xfitapplication.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.xfitapplication.domain.model.User
import com.example.xfitapplication.domain.repository.FoodRepository
import com.example.xfitapplication.presentation.RepositoryProvider

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FoodRepository = RepositoryProvider.getRepository(application)

    val user: LiveData<User?> = repository.getUser().asLiveData()
}

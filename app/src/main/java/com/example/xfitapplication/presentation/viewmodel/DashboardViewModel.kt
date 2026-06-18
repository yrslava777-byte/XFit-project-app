package com.example.xfitapplication.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.xfitapplication.domain.model.FoodEntry
import com.example.xfitapplication.domain.model.User
import com.example.xfitapplication.domain.repository.FoodRepository
import com.example.xfitapplication.domain.usecase.GetDailyProgressUseCase
import com.example.xfitapplication.presentation.RepositoryProvider
import com.example.xfitapplication.presentation.util.DateUtils
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FoodRepository = RepositoryProvider.getRepository(application)
    private val getDailyProgress = GetDailyProgressUseCase()
    private val today = DateUtils.todayDb()

    data class UiState(
        val user: User? = null,
        val progress: GetDailyProgressUseCase.DailyProgress? = null,
        val entries: List<FoodEntry> = emptyList()
    )

    val uiState: LiveData<UiState> = combine(
        repository.getUser(),
        repository.getEntriesByDate(today)
    ) { user, entries ->
        UiState(
            user = user,
            progress = getDailyProgress.execute(user, entries),
            entries = entries
        )
    }.asLiveData()

    init {
        viewModelScope.launch {
            try {
                repository.seedProductsIfEmpty()
            } catch (_: Exception) {
                // Не блокируем экран при ошибке инициализации базы
            }
        }
    }
}

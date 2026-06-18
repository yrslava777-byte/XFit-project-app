package com.example.xfitapplication.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.xfitapplication.domain.model.FoodEntry
import com.example.xfitapplication.domain.repository.FoodRepository
import com.example.xfitapplication.domain.usecase.GetDailyProgressUseCase
import com.example.xfitapplication.presentation.RepositoryProvider
import com.example.xfitapplication.presentation.util.DateUtils
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DiaryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FoodRepository = RepositoryProvider.getRepository(application)
    private val getDailyProgress = GetDailyProgressUseCase()
    private val today = DateUtils.todayDb()

    val entries: LiveData<List<FoodEntry>> =
        repository.getEntriesByDate(today).asLiveData()

    val totals: LiveData<GetDailyProgressUseCase.DailyProgress> =
        repository.getEntriesByDate(today)
            .map { entries -> getDailyProgress.execute(null, entries) }
            .asLiveData()

    fun deleteEntry(entry: FoodEntry) {
        viewModelScope.launch {
            try {
                repository.deleteEntry(entry)
            } catch (_: Exception) {
                // Ошибка удаления не должна приводить к падению приложения
            }
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            try {
                repository.deleteAllEntriesByDate(today)
            } catch (_: Exception) {
                // Ошибка очистки не должна приводить к падению приложения
            }
        }
    }
}

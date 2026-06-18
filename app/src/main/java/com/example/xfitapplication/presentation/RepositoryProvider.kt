package com.example.xfitapplication.presentation

import android.content.Context
import com.example.xfitapplication.data.local.AppDatabase
import com.example.xfitapplication.data.repository.FoodRepositoryImpl
import com.example.xfitapplication.domain.repository.FoodRepository

object RepositoryProvider {
    @Volatile
    private var repository: FoodRepository? = null

    fun getRepository(context: Context): FoodRepository {
        return repository ?: synchronized(this) {
            repository ?: createRepository(context).also { repository = it }
        }
    }

    private fun createRepository(context: Context): FoodRepository {
        val db = AppDatabase.getDatabase(context.applicationContext)
        return FoodRepositoryImpl(db.userDao(), db.productDao(), db.foodEntryDao())
    }
}

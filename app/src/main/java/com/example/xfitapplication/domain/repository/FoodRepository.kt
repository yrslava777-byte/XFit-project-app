package com.example.xfitapplication.domain.repository

import com.example.xfitapplication.domain.model.FoodEntry
import com.example.xfitapplication.domain.model.Product
import com.example.xfitapplication.domain.model.User
import kotlinx.coroutines.flow.Flow

interface FoodRepository {
    fun getUser(): Flow<User?>
    suspend fun getUserOnce(): User?
    suspend fun saveUser(user: User)

    fun getAllProducts(): Flow<List<Product>>
    fun searchProducts(query: String): Flow<List<Product>>
    suspend fun seedProductsIfEmpty()

    fun getEntriesByDate(date: String): Flow<List<FoodEntry>>
    suspend fun addEntry(entry: FoodEntry)
    suspend fun deleteEntry(entry: FoodEntry)
    suspend fun deleteAllEntriesByDate(date: String)
}

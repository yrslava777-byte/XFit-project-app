package com.example.xfitapplication.data.repository

import com.example.xfitapplication.data.local.*
import com.example.xfitapplication.domain.model.FoodEntry
import com.example.xfitapplication.domain.model.Product
import com.example.xfitapplication.domain.model.User
import kotlinx.coroutines.flow.Flow

class FoodRepository(
    private val userDao: UserDao,
    private val productDao: ProductDao,
    private val foodEntryDao: FoodEntryDao
) {
    // User
    fun getUser(userId: Int = 1): Flow<UserEntity> = userDao.getUserById(userId)
    suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)

    // Products
    fun getAllProducts(): Flow<List<ProductEntity>> = productDao.getAllProducts()
    fun searchProducts(query: String): Flow<List<ProductEntity>> = productDao.searchProducts(query)

    // Food Entries
    fun getEntriesByDate(date: String): Flow<List<FoodEntryEntity>> =
        foodEntryDao.getEntriesByDate(date)

    suspend fun addEntry(entry: FoodEntryEntity) = foodEntryDao.insertEntry(entry)
    suspend fun deleteEntry(entry: FoodEntryEntity) = foodEntryDao.deleteEntry(entry)
}
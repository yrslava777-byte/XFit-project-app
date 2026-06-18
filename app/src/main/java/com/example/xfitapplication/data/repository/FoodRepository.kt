package com.example.xfitapplication.data.repository

import com.example.xfitapplication.data.local.*
import kotlinx.coroutines.flow.Flow

class FoodRepository(
    private val userDao: UserDao,
    private val productDao: ProductDao,
    private val foodEntryDao: FoodEntryDao
) {
    // User
    fun getUser(): Flow<UserEntity?> = userDao.getUser()
    suspend fun insertUser(user: UserEntity) = userDao.insert(user)

    // Products
    fun getAllProducts(): Flow<List<ProductEntity>> = productDao.getAllProducts()
    fun searchProducts(query: String): Flow<List<ProductEntity>> = productDao.searchProducts(query)

    // Food Entries
    fun getEntriesByDate(date: String): Flow<List<FoodEntryEntity>> =
        foodEntryDao.getEntriesByDate(date)

    suspend fun addEntry(entry: FoodEntryEntity) = foodEntryDao.insertEntry(entry)
    suspend fun deleteEntry(entry: FoodEntryEntity) = foodEntryDao.deleteEntry(entry)
}
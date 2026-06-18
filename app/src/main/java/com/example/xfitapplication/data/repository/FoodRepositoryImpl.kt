package com.example.xfitapplication.data.repository

import com.example.xfitapplication.data.local.FoodEntryDao
import com.example.xfitapplication.data.local.ProductDao
import com.example.xfitapplication.data.local.ProductEntity
import com.example.xfitapplication.data.local.UserDao
import com.example.xfitapplication.data.mapper.toDomain
import com.example.xfitapplication.data.mapper.toEntity
import com.example.xfitapplication.domain.model.FoodEntry
import com.example.xfitapplication.domain.model.Product
import com.example.xfitapplication.domain.model.User
import com.example.xfitapplication.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FoodRepositoryImpl(
    private val userDao: UserDao,
    private val productDao: ProductDao,
    private val foodEntryDao: FoodEntryDao
) : FoodRepository {

    override fun getUser(): Flow<User?> =
        userDao.getUser().map { it?.toDomain() }

    override suspend fun getUserOnce(): User? =
        userDao.getUserOnce()?.toDomain()

    override suspend fun saveUser(user: User) {
        userDao.insert(user.toEntity())
    }

    override fun getAllProducts(): Flow<List<Product>> =
        productDao.getAllProducts().map { list -> list.map { it.toDomain() } }

    override fun searchProducts(query: String): Flow<List<Product>> =
        productDao.searchProducts(query).map { list -> list.map { it.toDomain() } }

    override suspend fun seedProductsIfEmpty() {
        if (productDao.getCount() > 0) return
        DEFAULT_PRODUCTS.forEach { productDao.insertProduct(it) }
    }

    override fun getEntriesByDate(date: String): Flow<List<FoodEntry>> =
        foodEntryDao.getEntriesByDate(date).map { list -> list.map { it.toDomain() } }

    override suspend fun addEntry(entry: FoodEntry) {
        foodEntryDao.insertEntry(entry.toEntity())
    }

    override suspend fun deleteEntry(entry: FoodEntry) {
        foodEntryDao.deleteEntry(entry.toEntity())
    }

    override suspend fun deleteAllEntriesByDate(date: String) {
        foodEntryDao.deleteAllByDate(date)
    }

    companion object {
        private val DEFAULT_PRODUCTS = listOf(
            ProductEntity(0, "Куриная грудка", 165.0, 31.0, 3.6, 0.0),
            ProductEntity(0, "Рис отварной", 130.0, 2.7, 0.3, 28.0),
            ProductEntity(0, "Гречка", 110.0, 4.2, 1.1, 20.0),
            ProductEntity(0, "Яблоко", 52.0, 0.3, 0.2, 14.0),
            ProductEntity(0, "Молоко 2.5%", 54.0, 2.8, 2.5, 4.7),
            ProductEntity(0, "Хлеб черный", 250.0, 8.5, 1.2, 48.0),
            ProductEntity(0, "Творог 5%", 120.0, 17.0, 5.0, 1.8),
            ProductEntity(0, "Яйцо куриное", 155.0, 13.0, 11.0, 1.1)
        )
    }
}

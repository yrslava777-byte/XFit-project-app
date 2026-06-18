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
        val existingNames = productDao.getAllProductNames().toSet()
        DEFAULT_PRODUCTS
            .filter { it.name !in existingNames }
            .forEach { productDao.insertProduct(it) }
    }

    override suspend fun addProduct(product: Product): Product {
        val entity = product.toEntity().copy(id = 0, isCustom = true)
        val id = productDao.insertCustomProduct(entity)
        return product.copy(id = id.toInt(), isCustom = true)
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
            ProductEntity(0, "Яйцо куриное", 155.0, 13.0, 11.0, 1.1),
            ProductEntity(0, "Овсянка", 342.0, 12.0, 6.0, 60.0),
            ProductEntity(0, "Банан", 89.0, 1.1, 0.3, 23.0),
            ProductEntity(0, "Лосось", 208.0, 20.0, 13.0, 0.0),
            ProductEntity(0, "Картофель отварной", 82.0, 2.0, 0.4, 17.0),
            ProductEntity(0, "Макароны отварные", 131.0, 5.0, 1.1, 25.0),
            ProductEntity(0, "Говядина", 187.0, 18.9, 12.4, 0.0),
            ProductEntity(0, "Сыр твёрдый", 350.0, 23.0, 28.0, 0.0),
            ProductEntity(0, "Огурец", 15.0, 0.8, 0.1, 3.6),
            ProductEntity(0, "Помидор", 20.0, 1.1, 0.2, 3.7),
            ProductEntity(0, "Масло сливочное", 748.0, 0.5, 82.5, 0.8),
            ProductEntity(0, "Йогурт натуральный", 60.0, 4.3, 3.2, 4.7),
            ProductEntity(0, "Индейка", 189.0, 21.6, 11.0, 0.0),
            ProductEntity(0, "Авокадо", 160.0, 2.0, 15.0, 9.0),
            ProductEntity(0, "Миндаль", 579.0, 21.0, 49.0, 22.0),
            ProductEntity(0, "Шпинат", 23.0, 2.9, 0.3, 2.0),
            ProductEntity(0, "Кефир 2.5%", 53.0, 3.0, 2.5, 4.0),
            ProductEntity(0, "Свинина", 242.0, 16.0, 19.0, 0.0),
            ProductEntity(0, "Апельсин", 43.0, 0.9, 0.2, 8.1),
            ProductEntity(0, "Брокколи", 34.0, 2.8, 0.4, 7.0),
            ProductEntity(0, "Тунец консервированный", 96.0, 21.0, 1.0, 0.0),
            ProductEntity(0, "Арахис", 567.0, 26.0, 49.0, 10.0),
            ProductEntity(0, "Сметана 20%", 206.0, 2.5, 20.0, 3.4),
            ProductEntity(0, "Капуста белокочанная", 27.0, 1.8, 0.1, 4.7),
            ProductEntity(0, "Морковь", 41.0, 0.9, 0.2, 9.6)
        )
    }
}

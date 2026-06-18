package com.example.xfitapplication.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>  // Flow вместо List

    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%'")
    fun searchProducts(query: String): Flow<List<ProductEntity>>  // Flow вместо List

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(product: ProductEntity)
}
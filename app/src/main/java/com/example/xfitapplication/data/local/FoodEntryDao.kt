package com.example.xfitapplication.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodEntryDao {
    @Query("SELECT * FROM food_entries WHERE mealDate = :date ORDER BY id")
    fun getEntriesByDate(date: String): Flow<List<FoodEntryEntity>>  // Flow вместо List

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: FoodEntryEntity)

    @Delete
    suspend fun deleteEntry(entry: FoodEntryEntity)

    @Query("DELETE FROM food_entries")
    suspend fun deleteAllEntries()
}
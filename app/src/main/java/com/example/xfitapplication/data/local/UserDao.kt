package com.example.xfitapplication.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users LIMIT 1")
    fun getUser(): Flow<UserEntity?>

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getUserOnce(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)
}
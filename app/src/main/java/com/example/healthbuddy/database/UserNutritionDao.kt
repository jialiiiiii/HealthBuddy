package com.example.healthbuddy.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface UserNutritionDao {
    @Insert
    suspend fun insert(userNutritionData: UserNutritionData)

    @Delete
    suspend fun delete(userNutritionData: UserNutritionData): Int

    @Update
    suspend fun update(userNutritionData: UserNutritionData)

//    @Query("SELECT * FROM user_nutrition_table")
//    fun getAllNutritionData()
}
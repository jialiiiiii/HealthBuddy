package com.example.healthbuddy.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserNutritionDao {
    @Insert
    suspend fun insertNutriData(userNutritionData: UserNutritionData)

    @Query("SELECT * FROM user_nutrition_table ORDER BY id DESC")
    fun getAllNutriData(): LiveData<List<UserNutritionData>>

    @Update
    suspend fun updateNutriData(userNutritionData: UserNutritionData)

    @Delete
    suspend fun deleteNutriData(userNutritionData: UserNutritionData): Int


}
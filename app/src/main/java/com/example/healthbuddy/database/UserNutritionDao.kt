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

    @Query("SELECT *, CAST(substr(intake_date, 4, 2) AS INTEGER) AS month FROM user_nutrition_table WHERE month == :month ORDER BY intake_date DESC")
    fun getAllNutriData(month: Int): LiveData<List<UserNutritionData>>

    @Query("SELECT date, day, SUM(cal_obtained) AS totalCaloriesGained FROM (SELECT substr(intake_date, 1, 10) AS date, CAST(substr(intake_date, 1, 2) AS INTEGER) AS day, CAST(substr(intake_date, 4, 2) AS INTEGER) AS month, cal_obtained FROM user_nutrition_table) WHERE month == :month GROUP BY date")
    fun getDailyCaloriesGained(month: Int): LiveData<List<TotalCaloriesGained>>

    @Update
    suspend fun updateNutriData(userNutritionData: UserNutritionData)

    @Delete
    suspend fun deleteNutriData(userNutritionData: UserNutritionData): Int


}
package com.example.healthbuddy.nutrition

import androidx.lifecycle.LiveData
import com.example.healthbuddy.database.TotalCaloriesBurnt
import com.example.healthbuddy.database.TotalCaloriesGained
import com.example.healthbuddy.database.UserExecData
import com.example.healthbuddy.database.UserNutritionDao
import com.example.healthbuddy.database.UserNutritionData
import java.util.Calendar

class NutriDataRepository(private val userNutritionDao: UserNutritionDao) {

    // Get the current date
    var calendar: Calendar = Calendar.getInstance()

    // Get the current month (0-based index, so add 1 to get the actual month)
    var currentMonth: Int = calendar.get(Calendar.MONTH) + 1

    val getAllNutriData: LiveData<List<UserNutritionData>> = userNutritionDao.getAllNutriData(currentMonth)
    val getDailyCaloriesGained: LiveData<List<TotalCaloriesGained>> = userNutritionDao.getDailyCaloriesGained(currentMonth)

    suspend fun addNutriData(userNutritionData: UserNutritionData){
        userNutritionDao.insertNutriData(userNutritionData)
    }

    suspend fun updateNutriData(userNutritionData: UserNutritionData){
        userNutritionDao.updateNutriData(userNutritionData)
    }

    suspend fun deleteNutriData(userNutritionData: UserNutritionData){
        userNutritionDao.deleteNutriData(userNutritionData)
    }
}
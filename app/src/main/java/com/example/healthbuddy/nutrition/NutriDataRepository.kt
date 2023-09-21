package com.example.healthbuddy.nutrition

import androidx.lifecycle.LiveData
import com.example.healthbuddy.database.UserExecData
import com.example.healthbuddy.database.UserNutritionDao
import com.example.healthbuddy.database.UserNutritionData

class NutriDataRepository(private val userNutritionDao: UserNutritionDao) {

    val getAllNutriData: LiveData<List<UserNutritionData>> = userNutritionDao.getAllNutriData()

    suspend fun addNutriData(userNutritionData: UserNutritionData){
        userNutritionDao.insertNutriData(userNutritionData)
    }
}
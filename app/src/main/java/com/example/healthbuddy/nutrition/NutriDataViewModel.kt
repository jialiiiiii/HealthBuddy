package com.example.healthbuddy.nutrition

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.healthbuddy.database.AppDatabase
import com.example.healthbuddy.database.TotalCaloriesBurnt
import com.example.healthbuddy.database.TotalCaloriesGained
import com.example.healthbuddy.database.UserExecData
import com.example.healthbuddy.database.UserNutritionData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class NutriDataViewModel(application: Application): AndroidViewModel(application) {

    val getAllNutriData: LiveData<List<UserNutritionData>>
    val getTotalCaloriesGained: LiveData<List<TotalCaloriesGained>>
    private val nutriDataRepository: NutriDataRepository

    init{
        val userNutriDao = AppDatabase.getDatabase(application).userNutritionDao()
        nutriDataRepository = NutriDataRepository(userNutriDao)
        getAllNutriData = nutriDataRepository.getAllNutriData
        getTotalCaloriesGained = nutriDataRepository.getDailyCaloriesGained
    }

    fun insertNutriData(userNutritionData: UserNutritionData){
        try{
            viewModelScope.launch(Dispatchers.IO) {
                nutriDataRepository.addNutriData(userNutritionData)
            }
        }catch (e: Exception){

        }
    }

    fun updateNutriData(userNutritionData: UserNutritionData){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                nutriDataRepository.updateNutriData(userNutritionData)
            }
        } catch (e: Exception) {
            // Handle the exception (e.g., log it or display an error message)
        }
    }

    fun deleteNutriData(userNutritionData: UserNutritionData){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                nutriDataRepository.deleteNutriData(userNutritionData)
            }
        } catch (e: Exception) {
            // Handle the exception (e.g., log it or display an error message)
        }
    }
}
package com.example.healthbuddy.nutrition

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.healthbuddy.database.AppDatabase
import com.example.healthbuddy.database.UserNutritionData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class NutriDataViewModel(application: Application): AndroidViewModel(application) {

    private val getAllNutriData: LiveData<List<UserNutritionData>>
    private val nutriDataRepository: NutriDataRepository

    init{
        val userNutriDao = AppDatabase.getDatabase(application).userNutritionDao()
        nutriDataRepository = NutriDataRepository(userNutriDao)
        getAllNutriData = nutriDataRepository.getAllNutriData
    }

    fun insertNutriData(userNutritionData: UserNutritionData){
        try{
            viewModelScope.launch(Dispatchers.IO) {
                nutriDataRepository.addNutriData(userNutritionData)
            }
        }catch (e: Exception){

        }
    }
}
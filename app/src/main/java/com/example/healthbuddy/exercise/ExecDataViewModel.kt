package com.example.healthbuddy.exercise

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthbuddy.database.AppDatabase
import com.example.healthbuddy.database.TotalCaloriesBurnt
import com.example.healthbuddy.database.UserExecData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExecDataViewModel(application: Application): AndroidViewModel(application) {

    val getAllExecData: LiveData<List<UserExecData>>
    val getTotalCaloriesBurnt: LiveData<List<TotalCaloriesBurnt>>
    private val execDataRepository: ExecDataRepository

    init{
        val userExecDao = AppDatabase.getDatabase(application).userExecDao()
        execDataRepository = ExecDataRepository(userExecDao)
        getAllExecData = execDataRepository.getAllExecData
        getTotalCaloriesBurnt = execDataRepository.getDailyCaloriesBurnt
    }

    fun insertExecData(userExecData: UserExecData){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                execDataRepository.addExecData(userExecData)
            }
        } catch (e: Exception) {
            // Handle the exception (e.g., log it or display an error message)
        }
    }

    fun deleteExecData(userExecData: UserExecData){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                execDataRepository.deleteExecData(userExecData)
            }
        } catch (e: Exception) {
            // Handle the exception (e.g., log it or display an error message)
        }
    }

}
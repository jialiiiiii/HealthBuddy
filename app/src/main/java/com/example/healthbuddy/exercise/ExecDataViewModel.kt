package com.example.healthbuddy.exercise

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.healthbuddy.database.AppDatabase
import com.example.healthbuddy.database.UserExecData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExecDataViewModel(application: Application): AndroidViewModel(application) {

    private val getAllExecData: LiveData<List<UserExecData>>
    private val execDataRepository: ExecDataRepository

    init{
        val userExecDao = AppDatabase.getDatabase(application).userExecDao()
        execDataRepository = ExecDataRepository(userExecDao)
        getAllExecData = execDataRepository.getAllExecData
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

}
package com.example.healthbuddy.exercise

import androidx.lifecycle.LiveData
import com.example.healthbuddy.database.TotalCaloriesBurnt
import com.example.healthbuddy.database.UserExecDao
import com.example.healthbuddy.database.UserExecData
import java.util.Calendar

class ExecDataRepository(private val userExecDao: UserExecDao) {

    // Get the current date
    var calendar: Calendar = Calendar.getInstance()

    // Get the current month (0-based index, so add 1 to get the actual month)
    var currentMonth: Int = calendar.get(Calendar.MONTH) + 1

    val getAllExecData: LiveData<List<UserExecData>> = userExecDao.getAllExecData(currentMonth)
    val getDailyCaloriesBurnt: LiveData<List<TotalCaloriesBurnt>> = userExecDao.getDailyCaloriesBurnt(currentMonth)
//    val getDailyCaloriesBurnt: LiveData<List<TotalCaloriesBurnt>> = userExecDao.getDailyCaloriesBurnt(currentMonth)

    suspend fun addExecData(userExecData: UserExecData){
        userExecDao.insertExecData(userExecData)
    }

    suspend fun updateExecData(userExecData: UserExecData){
        userExecDao.updateExecData(userExecData)
    }

    suspend fun deleteExecData(userExecData: UserExecData){
        userExecDao.deleteExecData(userExecData)
    }

}
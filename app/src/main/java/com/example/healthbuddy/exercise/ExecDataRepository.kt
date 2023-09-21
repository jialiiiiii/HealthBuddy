package com.example.healthbuddy.exercise

import androidx.lifecycle.LiveData
import com.example.healthbuddy.database.UserExecDao
import com.example.healthbuddy.database.UserExecData

class ExecDataRepository(private val userExecDao: UserExecDao) {

    val getAllExecData: LiveData<List<UserExecData>> = userExecDao.getAllExecData()

    suspend fun addExecData(userExecData: UserExecData){
        userExecDao.insertExecData(userExecData)
    }
}
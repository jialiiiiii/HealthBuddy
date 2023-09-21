package com.example.healthbuddy.exercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.healthbuddy.database.UserExecDao
import com.example.healthbuddy.database.UserExecData

class ExecDataItemViewModel(private val userExecDao: UserExecDao) : ViewModel() {
    val userExecDataList: LiveData<List<UserExecData>> = userExecDao.getAllExecData()

}
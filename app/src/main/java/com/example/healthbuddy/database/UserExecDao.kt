package com.example.healthbuddy.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserExecDao {
    @Insert
    suspend fun insertExecData(userExecData: UserExecData)

    @Query("SELECT * FROM user_exec_table ORDER BY exec_date DESC")
    fun getAllExecData(): LiveData<List<UserExecData>>

    @Query("SELECT date, day, SUM(cal_burnt) AS totalCaloriesBurnt FROM (SELECT substr(exec_date, 1, 10) AS date, CAST(substr(exec_date, 1, 2) AS INTEGER) AS day, cal_burnt FROM user_exec_table) GROUP BY date")
    fun getDailyCaloriesBurnt(): LiveData<List<TotalCaloriesBurnt>>

    @Update
    suspend fun updateExecData(userExecData: UserExecData)

    @Delete
    suspend fun deleteExecData(userExecData: UserExecData): Int
}

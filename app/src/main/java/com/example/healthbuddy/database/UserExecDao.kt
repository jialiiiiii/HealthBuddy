package com.example.healthbuddy.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.time.Month




@Dao
interface UserExecDao {
    @Insert
    suspend fun insertExecData(userExecData: UserExecData)

    @Query("SELECT *, CAST(substr(exec_date, 4, 2) AS INTEGER) AS month FROM user_exec_table WHERE month == :month ORDER BY exec_date DESC, exec_time DESC")
    fun getAllExecData(month: Int): LiveData<List<UserExecData>>

//    @Query("SELECT date, day, SUM(cal_burnt) AS totalCaloriesBurnt FROM (SELECT substr(exec_date, 1, 10) AS date, CAST(substr(exec_date, 1, 2) AS INTEGER) AS day, CAST(substr(exec_date, 4, 2) AS INTEGER) AS month, cal_burnt FROM user_exec_table) GROUP BY date")
//    fun getDailyCaloriesBurnt(): LiveData<List<TotalCaloriesBurnt>>

    @Query("SELECT date, day, SUM(cal_burnt) AS totalCaloriesBurnt FROM (SELECT substr(exec_date, 1, 10) AS date, CAST(substr(exec_date, 1, 2) AS INTEGER) AS day, CAST(substr(exec_date, 4, 2) AS INTEGER) AS month, cal_burnt FROM user_exec_table) WHERE month == :month GROUP BY date")
    fun getDailyCaloriesBurnt(month: Int): LiveData<List<TotalCaloriesBurnt>>

    @Update
    suspend fun updateExecData(userExecData: UserExecData)

    @Delete
    suspend fun deleteExecData(userExecData: UserExecData): Int
}

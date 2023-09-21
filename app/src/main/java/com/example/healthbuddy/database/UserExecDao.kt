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

    @Query("SELECT * FROM user_exec_table ORDER BY id DESC")
    fun getAllExecData(): LiveData<List<UserExecData>>

    @Update
    suspend fun updateExecData(userExecData: UserExecData)

    @Delete
    suspend fun deleteExecData(userExecData: UserExecData): Int
}

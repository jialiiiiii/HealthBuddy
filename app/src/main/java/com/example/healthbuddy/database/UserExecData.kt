package com.example.healthbuddy.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_exec_table")
data class UserExecData (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "exec_category") var exec_category: String,
    @ColumnInfo(name = "exec_type") var exec_type: String,
    @ColumnInfo(name = "exec_duration_rep") var exec_duration_rep: Int,
    @ColumnInfo(name = "exec_set") var exec_set: Int,
    @ColumnInfo(name = "exec_date") var exec_date: String,
    @ColumnInfo(name = "exec_time") var exec_time: String,
    @ColumnInfo(name = "cal_burnt") var cal_burnt: Double
)
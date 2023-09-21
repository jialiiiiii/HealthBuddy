package com.example.healthbuddy.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_nutrition_table")
class UserNutritionData(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "food_category") var food_category: String,
    @ColumnInfo(name = "food_type") var food_type: String,
    @ColumnInfo(name = "food_size") var food_size: Int,
    @ColumnInfo(name = "food_serving_size") var food_serving_size: Int,
    @ColumnInfo(name = "intake_date") var intake_date: String,
    @ColumnInfo(name = "intake_time") var intake_time: String,
    @ColumnInfo(name = "cal_obtained") var cal_obtained: Double
)
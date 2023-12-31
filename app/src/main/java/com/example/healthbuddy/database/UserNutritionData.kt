package com.example.healthbuddy.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user_nutrition_table")
class UserNutritionData(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "food_category") var food_category: Int,
    @ColumnInfo(name = "food_type") var food_type: Int,
    @ColumnInfo(name = "food_size") var food_size: Int,
    @ColumnInfo(name = "food_serving_size") var food_serving_size: Int,
    @ColumnInfo(name = "intake_date") var intake_date: String,
    @ColumnInfo(name = "intake_time") var intake_time: String,
    @ColumnInfo(name = "cal_obtained") var cal_obtained: Double
): Parcelable
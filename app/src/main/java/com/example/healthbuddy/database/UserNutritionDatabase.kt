package com.example.healthbuddy.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserNutritionData::class], version = 1, exportSchema = false)
abstract class UserNutritionDatabase : RoomDatabase(){
    abstract val userNutritionDao: UserNutritionDao

    companion object{
        @Volatile
        private var INSTANCE: UserNutritionDatabase? = null

        fun getInstance(context: Context): UserNutritionDatabase {
            // Make sure only single thread of execution
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserNutritionDatabase::class.java,
                        "user_nutrition_db")
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}
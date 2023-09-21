package com.example.healthbuddy.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserExecData::class], version = 1, exportSchema = false)
public abstract class UserExecDatabase : RoomDatabase() {
    abstract fun userExecDao(): UserExecDao

    companion object {
        @Volatile
        private var INSTANCE: UserExecDatabase? = null

        fun getInstance(context: Context): UserExecDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserExecDatabase::class.java, "user_exec_db"
                )
                    .fallbackToDestructiveMigration() // Schema migration strategy
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
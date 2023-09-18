//package com.example.healthbuddy.database
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//
//@Database(entities = [User::class], version = 1, exportSchema = false)
//abstract class HealthDatabase : RoomDatabase() {
//    abstract fun userDao(): UserDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: HealthDatabase? = null
//
//        fun getDatabase(context: Context): HealthDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    HealthDatabase::class.java,
//                    "heath-database"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}

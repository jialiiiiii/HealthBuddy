//package com.example.healthbuddy.database
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.Query
//
//@Dao
//interface UserDao {
//    @Insert
//    suspend fun insert(user: User)
//
//    @Query("SELECT * FROM users WHERE id = :userId")
//    fun getUserById(userId: String): User?
//}
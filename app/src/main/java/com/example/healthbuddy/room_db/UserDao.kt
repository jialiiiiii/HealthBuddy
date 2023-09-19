package com.example.healthbuddy.room_db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User) : Long

    @Delete
    fun deleteUser(user: User)

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: String): User?
}
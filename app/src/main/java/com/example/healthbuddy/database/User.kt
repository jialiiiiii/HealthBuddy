package com.example.healthbuddy.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tokenId: String?,
    val name: String?,
    val email: String?
)
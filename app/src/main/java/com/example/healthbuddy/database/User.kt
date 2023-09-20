package com.example.healthbuddy.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tokenId: String?,
    val name: String?,
    val email: String?,
    val gender: String? = "",
    val age: Int? = 0,
    val weight: Double? = 0.0,
    val height: Double? = 0.0
)
package com.example.healthbuddy.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: String,
    var name: String? = "",
    val email: String? = "",
    var gender: String? = "",
    var age: Int? = 0,
    var weight: Double? = 0.0,
    var height: Double? = 0.0
){
    constructor() : this("")
}
package com.example.jomeco.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val profilePictureUri: String? = null
)


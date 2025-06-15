package com.example.jomeco.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_registration")
data class EventRegistration(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val eventId: Int,
    val userId: Int,
    val fullName: String,
    val nric: String,
    val email: String,
    val phoneNumber: String,
    val emergencyName: String,
    val emergencyNumber: String,
    val registrationTime: Long = System.currentTimeMillis()
)

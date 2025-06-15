package com.example.jomeco.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val location: String,
    val date: String,
    val time: String,
    val points: Int,
    val hours: Int,
    val description: String,
    val imageUrl: String
)

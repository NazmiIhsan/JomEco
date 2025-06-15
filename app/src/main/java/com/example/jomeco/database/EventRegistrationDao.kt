package com.example.jomeco.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventRegistrationDao {

    // Insert a new registration
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistration(registration: EventRegistration)

    // Get all registrations
    @Query("SELECT * FROM event_registration")
    fun getAllRegistrations(): Flow<List<EventRegistration>>

    // Get registrations by event ID
    @Query("SELECT * FROM event_registration WHERE eventId = :eventId")
    fun getRegistrationsForEvent(eventId: Int): Flow<List<EventRegistration>>

    // Get a single registration by ID (optional)
    @Query("SELECT * FROM event_registration WHERE id = :id")
    suspend fun getRegistrationById(id: Int): EventRegistration?

    @Query("SELECT COUNT(*) FROM event_registration WHERE eventId = :eventId AND userId = :userId")
    fun hasUserJoined(eventId: Int, userId: Int): Flow<Int>




}

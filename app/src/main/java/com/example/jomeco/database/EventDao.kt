package com.example.jomeco.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Query("SELECT * FROM events")
    fun getAllEvents(): Flow<List<Event>>

//    @Query("SELECT * FROM events WHERE id = :eventId")
//    suspend fun getEventById(eventId: Int): Event?

    @Delete
    suspend fun deleteEvent(event: Event)

    @Query("DELETE FROM events")
    suspend fun deleteAllEvents()


//    @Query("SELECT * FROM events WHERE id = :eventId")
//    suspend fun getEventById(eventId: Int): Event?

    @Query("SELECT * FROM events WHERE id = :eventId")
    fun getEventById(eventId: Int): Flow<Event?>






    @Query("""
    SELECT * FROM events
    WHERE id NOT IN (
        SELECT eventId FROM event_registration WHERE userId = :currentUserId
    )
""")
    fun getUnjoinedEvents(currentUserId: Int): Flow<List<Event>>







}

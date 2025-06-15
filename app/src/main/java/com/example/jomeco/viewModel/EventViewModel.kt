package com.example.jomeco.viewModel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jomeco.database.AppDatabase
import com.example.jomeco.database.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.LocalDate
import java.time.LocalTime


class EventViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val eventDao = db.eventDao()
    val eventsFlow: Flow<List<Event>> = eventDao.getAllEvents() // make sure this DAO function exists
    @RequiresApi(Build.VERSION_CODES.O)
    val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH)
    @RequiresApi(Build.VERSION_CODES.O)
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a",   Locale.ENGLISH)



    @RequiresApi(Build.VERSION_CODES.O)
    fun insertSampleEvent() {
        viewModelScope.launch(Dispatchers.IO) {
            val event = Event(
                title = "Keluarga Malaysia Run - Earth Day 2025",
                date = LocalDate.parse("21 June 2025", dateFormatter).toString(),
                time = LocalTime.parse("08:00 AM", timeFormatter).toString(),
                location = "DBKL, Jalan Raja Laut",
                points = 100,
                hours = 2,
                description = "Join the Earth Day Run in Kuala Lumpur for a greener Malaysia!",
                imageUrl = "eco1"
            )

           val event1 =  Event(
                title = "Beach Cleanup Drive - Selamatkan Pantai",
               date = LocalDate.parse("5 August 2025", dateFormatter).toString(),
                time = LocalTime.parse("07:00 AM", timeFormatter).toString(),
                location = "Pantai Morib, Selangor",
                points = 90,
                hours = 2,
                description = "Help clean the beach and preserve marine ecosystems.",
                imageUrl = "eco2"
            )
            val event2 = Event(
                title = "Community Recycling Day",
                date = LocalDate.parse("10 September 2025", dateFormatter).toString(),
                time = LocalTime.parse("10:00 AM", timeFormatter).toString(),
                location = "Subang Jaya Community Hall",
                points = 80,
                hours = 1,
                description = "Bring your recyclables and earn points while helping the planet.",
                imageUrl = "eco3"
            )
            eventDao.insertEvent(event)
            eventDao.insertEvent(event1)
            eventDao.insertEvent(event2)
        }

    }

    fun deleteAllEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            eventDao.deleteAllEvents()
        }
    }


    fun getEventById(eventId: Int): Flow<Event?> {
        return eventDao.getEventById(eventId)
    }

    fun getEventsNotJoinedBy(userId: Int): Flow<List<Event>> {
        return eventDao.getUnjoinedEvents(userId)
    }



}

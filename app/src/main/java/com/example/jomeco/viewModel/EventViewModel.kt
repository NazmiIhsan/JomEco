package com.example.jomeco.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jomeco.database.AppDatabase
import com.example.jomeco.database.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EventViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val eventDao = db.eventDao()
    val eventsFlow: Flow<List<Event>> = eventDao.getAllEvents() // make sure this DAO function exists


    fun insertSampleEvent() {
        viewModelScope.launch(Dispatchers.IO) {
            val event = Event(
                title = "Keluarga Malaysia Run - Earth Day 2022",
                date = "24 September 2022",
                time = "08:00 AM",
                location = "DBKL, Jalan Raja Laut",
                points = 50,
                hours = 2,
                description = "Join the Earth Day Run in Kuala Lumpur for a greener Malaysia!",
                imageUrl = "eco1.jpg" // drawable/run2022.png
            )
            eventDao.insertEvent(event)
        }
    }
}

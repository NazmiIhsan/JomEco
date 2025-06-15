package com.example.jomeco.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jomeco.database.EventRegistration
import com.example.jomeco.database.EventRegistrationDao
import com.example.jomeco.viewModel.EventRegistrationViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EventRegistrationViewModel(
    private val eventRegistrationDao: EventRegistrationDao
) : ViewModel() {

    // Get all registrations (optional)
    val allRegistrations: Flow<List<EventRegistration>> = eventRegistrationDao.getAllRegistrations()

    // Insert a new registration
    fun insertRegistration(registration: EventRegistration) {
        viewModelScope.launch {
            eventRegistrationDao.insertRegistration(registration)
        }
    }

    // Get participants by event
    fun getRegistrationsByEventId(eventId: Int): Flow<List<EventRegistration>> {
        return eventRegistrationDao.getRegistrationsForEvent(eventId)
    }



}

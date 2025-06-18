package com.example.jomeco.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jomeco.database.EventDao
import com.example.jomeco.database.EventRegistration
import com.example.jomeco.database.EventRegistrationDao
import com.example.jomeco.database.UserDao
import com.example.jomeco.viewModel.EventRegistrationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class EventRegistrationViewModel(
    private val userDao: UserDao,
    private val eventDao: EventDao,
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

//    fun deleteAllRegistrations() {
//        viewModelScope.launch {
//            eventRegistrationDao.deleteAllRegistrations()
//        }
//    }


    fun deleteAllRegistrations() {
        viewModelScope.launch(Dispatchers.IO) {
            eventRegistrationDao.deleteAllRegistrations()
        }
    }


    suspend fun joinEventAndAddPoints(
        userId: Int,
        eventId: Int,
        fullName: String,
        nric: String,
        email: String,
        phone: String,
        emergencyName: String,
        emergencyPhone: String
    ) {
        val user = userDao.getUserByIds(userId)
        val event = eventDao.getEventById(eventId).firstOrNull()

        if (user != null && event != null) {
            val newTotalPoints = user.totalPoints + event.points
            val updatedUser = user.copy(totalPoints = newTotalPoints)

            userDao.updateUser(updatedUser)

            val registration = EventRegistration(
                userId = userId,
                eventId = eventId,
                fullName = fullName,
                nric = nric,
                email = email,
                phoneNumber = phone,
                emergencyName = emergencyName,
                emergencyNumber = emergencyPhone
            )

            eventRegistrationDao.insertRegistration(registration)
        } else {
            println("Error: User or Event not found.")
        }
    }








}

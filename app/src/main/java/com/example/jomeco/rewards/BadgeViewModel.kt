package com.example.profile.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jomeco.R
import com.example.jomeco.database.AppDatabase
import com.example.jomeco.database.toBadge
import com.example.jomeco.rewards.Badge
import com.example.jomeco.rewards.UserBadgeCrossRef
import com.example.profile.data.BadgeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.find
import kotlinx.coroutines.flow.first


class BadgeViewModel(application: Application) : AndroidViewModel(application) {

    private val badgeDao = AppDatabase.getDatabase(application).badgeDao()
    private val userDao = AppDatabase.getDatabase(application).userDao()
    private val userBadgeDao = AppDatabase.getDatabase(application).userBadgeDao()
    private val repository = BadgeRepository(badgeDao, userBadgeDao)

    private val _badges = mutableStateOf<List<Badge>>(emptyList())
    val badges: State<List<Badge>> = _badges



    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _userPoints = mutableStateOf(0)
    val userPoints: State<Int> = _userPoints

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _successMessage = mutableStateOf<String?>(null)
    val successMessage: State<String?> = _successMessage


    init {
        viewModelScope.launch {
            initializeDefaultBadges()
            loadBadges()
            loadUserPoints()

            val userId = getCurrentUserId()
            if (userId != null) {
                loadBadgesForUser(userId)
            }
        }
    }

    private fun getCurrentUserId(): Int? {
        val sharedPref = getApplication<Application>()
            .getSharedPreferences("jomeco_prefs", Context.MODE_PRIVATE)
        return if (sharedPref.contains("current_user_id")) {
            sharedPref.getInt("current_user_id", -1).takeIf { it != -1 }
        } else null
    }

    private fun loadBadges() {
        viewModelScope.launch {
            repository.getAllBadges().collect { badgeList ->
                _badges.value = badgeList
                _isLoading.value = false
            }
        }
    }

    private fun initializeDefaultBadges() {
        viewModelScope.launch {
            val existingBadges = repository.getAllBadges().first()
            if (existingBadges.isEmpty()) {
                val defaultBadges = getDefaultBadges()
                repository.insertInitialBadges(defaultBadges)
            }
        }
    }

    fun claimBadge(badgeId: String) {
        viewModelScope.launch {
            val userId = getCurrentUserId()
            if (userId != null) {
                val badge = badgeDao.getBadgeById(badgeId)
                val user = userDao.getUserByIdOnce(userId)

                if (user.totalPoints >= badge.pointsReward) {
                    // 1. Deduct points
                    val updatedUser = user.copy(totalPoints = user.totalPoints - badge.pointsReward)
                    userDao.updateUser(updatedUser)

                    // 2. Insert into UserBadgeCrossRef
                    val crossRef = UserBadgeCrossRef(userId = user.id, badgeId = badge.id)
                    userBadgeDao.insertUserBadgeCrossRef(crossRef)


                    loadBadgesForUser(userId)


                    _successMessage.value = "Badge claimed successfully"
                } else {
                    _errorMessage.value = "Not enough points to claim this badge"
                }
            }
        }
    }


    fun loadBadgesForUser(userId: Int) {
        viewModelScope.launch {
            try {
                val allBadges = badgeDao.getAllBadges().first()
                val earnedBadges = userBadgeDao.getUserWithBadges(userId)?.badges ?: emptyList()

                val badgeUIList = allBadges.map { badge ->
                    badge.toBadge().copy(
                        isEarned = earnedBadges.any { it.id == badge.id }
                    )
                }

                _badges.value = badgeUIList
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load badges"
            }
        }
    }



    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun clearSuccessMessage() {
        _successMessage.value = null
    }

    private fun loadUserPoints() {
        viewModelScope.launch {
            val userId = getCurrentUserId()
            if (userId != null) {
                userDao.getUserById(userId).collect { user ->
                    _userPoints.value = user.totalPoints
                }
            }
        }
    }

    private fun getDefaultBadges(): List<Badge> {
        return listOf(
            Badge(
                id = "1",
                name = "Tree Planter",
                description = "",
                iconResId = R.drawable.treeplanter,
                color = Color(0xFF4CAF50),
                eventName = "",
                isEarned = false,
                pointsReward = 10
            ),
            Badge(
                id = "2",
                name = "Eco Warrior",
                description = "",
                iconResId = R.drawable.ecowarrior,
                color = Color(0xFF2196F3),
                eventName = "",
                isEarned = false,
                pointsReward = 20
            ),
            Badge(
                id = "3",
                name = "Water Saver",
                description = "",
                iconResId = R.drawable.watersaver,
                color = Color(0xFF00BCD4),
                eventName = "",
                isEarned = false,
                pointsReward = 30
            ),
            Badge(
                id = "4",
                name = "Solar Champion",
                description = "",
                iconResId = R.drawable.solarchampion,
                color = Color(0xFFFF9800),
                eventName = "",
                isEarned = false,
                pointsReward = 40
            ),
            Badge(
                id = "5",
                name = "Recycling Hero",
                description = "",
                iconResId = R.drawable.recyclinghero,
                color = Color(0xFF9C27B0),
                eventName = "",
                isEarned = false,
                pointsReward = 50
            ),
            Badge(
                id = "6",
                name = "Clean Air Advocate",
                description = "",
                iconResId = R.drawable.cleanairadvocate,
                color = Color(0xFF607D8B),
                eventName = "",
                isEarned = false,
                pointsReward = 60
            )
        )
    }
}

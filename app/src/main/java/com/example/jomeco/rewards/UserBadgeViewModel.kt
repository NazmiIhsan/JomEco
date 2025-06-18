package com.example.jomeco.rewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserBadgeViewModel(
    private val userBadgeDao: UserBadgeDao
) : ViewModel() {

    fun assignBadge(userId: Int, badgeId: String) {
        viewModelScope.launch {
            val crossRef = UserBadgeCrossRef(userId = userId, badgeId = badgeId)
            userBadgeDao.insertUserBadgeCrossRef(crossRef)
        }
    }

    suspend fun getBadges(userId: Int): UserWithBadges? {
        return userBadgeDao.getUserWithBadges(userId)
    }
}


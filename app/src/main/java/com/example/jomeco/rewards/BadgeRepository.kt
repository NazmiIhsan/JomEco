package com.example.profile.data
import com.example.jomeco.database.toBadge
import com.example.jomeco.database.toBadgeEntity
import com.example.jomeco.rewards.Badge
import com.example.jomeco.rewards.BadgeDao
import com.example.jomeco.rewards.UserBadgeCrossRef
import com.example.jomeco.rewards.UserBadgeDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class BadgeRepository(private val badgeDao: BadgeDao,  private val userBadgeDao: UserBadgeDao) {

    fun getAllBadges(): Flow<List<Badge>> {
        return badgeDao.getAllBadges().map { entities ->
            entities.map { it.toBadge() }
        }
    }

//    fun getEarnedBadges(): Flow<List<Badge>> {
//        return badgeDao.getEarnedBadges().map { entities ->
//            entities.map { it.toBadge() }
//        }
//    }

//    fun getAvailableBadges(): Flow<List<Badge>> {
//        return badgeDao.getAvailableBadges().map { entities ->
//            entities.map { it.toBadge() }
//        }
//    }

    suspend fun insertBadge(badge: Badge) {
        badgeDao.insertBadge(badge.toBadgeEntity())
    }

    suspend fun insertInitialBadges(badges: List<Badge>) {
        badgeDao.insertAllBadges(badges.map { it.toBadgeEntity() })
    }

    suspend fun claimBadge(userId: Int, badgeId: String, dateEarned: String) {
        // Optional: update badge dateEarned if you still want to store per-user earned date somewhere
        val crossRef = UserBadgeCrossRef(userId = userId, badgeId = badgeId)
        userBadgeDao.insertUserBadgeCrossRef(crossRef)
    }


    suspend fun updateBadge(badge: Badge) {
        badgeDao.updateBadge(badge.toBadgeEntity())
    }

    suspend fun getBadgeById(badgeId: String): Badge {
        return badgeDao.getBadgeById(badgeId).toBadge()
    }

    suspend fun getBadgesForUser(userId: Int): List<Badge> {
        val allBadges = badgeDao.getAllBadges().first()
        val earnedBadges = userBadgeDao.getUserWithBadges(userId)?.badges ?: emptyList()

        return allBadges.map { badgeEntity ->
            val isEarned = earnedBadges.any { it.id == badgeEntity.id }
            badgeEntity.toBadge().copy(isEarned = isEarned)
        }
    }


}
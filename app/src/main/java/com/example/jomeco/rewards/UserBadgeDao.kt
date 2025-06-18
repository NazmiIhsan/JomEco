package com.example.jomeco.rewards


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface UserBadgeDao {

    // Insert CrossRef
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserBadgeCrossRef(crossRef: UserBadgeCrossRef)

//    // Get all badges for a user
//    @Transaction
//    @Query("SELECT * FROM user WHERE id = :userId")
//    fun getUserWithBadges(userId: Int): Flow<UserWithBadges>

    // Get all users who earned a badge
    @Transaction
    @Query("SELECT * FROM badges WHERE id = :badgeId")
    fun getBadgeWithUsers(badgeId: String): Flow<BadgeWithUsers>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userBadgeCrossRef: UserBadgeCrossRef)


    @Transaction
    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getUserWithBadges(userId: Int): UserWithBadges?

}

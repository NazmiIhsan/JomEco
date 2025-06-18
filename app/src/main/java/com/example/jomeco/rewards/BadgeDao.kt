package com.example.jomeco.rewards

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.jomeco.database.BadgeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BadgeDao {
    @Query("SELECT * FROM badges")
    fun getAllBadges(): Flow<List<BadgeEntity>>

//    @Query("SELECT * FROM badges WHERE isEarned = 1")
//    fun getEarnedBadges(): Flow<List<BadgeEntity>>

//    @Query("SELECT * FROM badges WHERE isEarned = 0")
//    fun getAvailableBadges(): Flow<List<BadgeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadge(badge: BadgeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBadges(badges: List<BadgeEntity>)

    @Update
    suspend fun updateBadge(badge: BadgeEntity)

//    @Query("UPDATE badges SET isEarned = 1, dateEarned = :dateEarned WHERE id = :badgeId")
//    suspend fun claimBadge(badgeId: String, dateEarned: String)

    @Delete
    suspend fun deleteBadge(badge: BadgeEntity)

    @Query("DELETE FROM badges")
    suspend fun deleteAllBadges()

    @Query("SELECT * FROM badges WHERE id = :id")
    suspend fun getBadgeById(id: String): BadgeEntity


}
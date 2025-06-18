package com.example.jomeco.rewards

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.jomeco.database.User
import com.example.jomeco.database.BadgeEntity

data class UserWithBadges(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "id", // <- still correct for BadgeEntity
        associateBy = Junction(
            value = UserBadgeCrossRef::class,
            parentColumn = "userId",
            entityColumn = "badgeId" // <- correct
        )
    )
    val badges: List<BadgeEntity>
)


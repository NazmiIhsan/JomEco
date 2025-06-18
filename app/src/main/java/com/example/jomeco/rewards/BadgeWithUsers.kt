package com.example.jomeco.rewards

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.jomeco.database.User
import com.example.jomeco.database.BadgeEntity

data class BadgeWithUsers(
    @Embedded val badge: BadgeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = UserBadgeCrossRef::class,
            parentColumn = "badgeId",
            entityColumn = "userId"
        )
    )
    val users: List<User>
)

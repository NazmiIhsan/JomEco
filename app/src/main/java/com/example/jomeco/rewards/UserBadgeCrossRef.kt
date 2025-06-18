package com.example.jomeco.rewards

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.jomeco.database.User
import com.example.jomeco.database.BadgeEntity


@Entity(
    tableName = "user_badge_cross_ref",
    primaryKeys = ["userId", "badgeId"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BadgeEntity::class,
            parentColumns = ["id"],
            childColumns = ["badgeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userId"]),
        Index(value = ["badgeId"])
    ]
)
data class UserBadgeCrossRef(
    val userId: Int,
    val badgeId: String
)

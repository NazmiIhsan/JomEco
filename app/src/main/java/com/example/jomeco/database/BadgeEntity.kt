package com.example.jomeco.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.example.jomeco.rewards.Badge

@Entity(tableName = "badges")
data class BadgeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val iconResId: Int,    // e.g., "park"
    val colorHex: Long,   // e.g., 0xFF4CAF50
    val eventName: String,
//    val isEarned: Boolean = false,
//    val dateEarned: String? = null,
    val pointsReward: Int
)

// -- Conversion: Entity to UI Model
fun BadgeEntity.toBadge(): Badge {
    return Badge(
        id = id,
        name = name,
        description = description,
        iconResId = iconResId,
        color = Color(colorHex),
        eventName = eventName,
//        isEarned = isEarned,
//        dateEarned = dateEarned,
        pointsReward = pointsReward
    )
}



// -- Conversion: UI Model to Entity
fun Badge.toBadgeEntity(): BadgeEntity {
    return BadgeEntity(
        id = id,
        name = name,
        description = description,
        iconResId = iconResId,
        colorHex = color.value.toLong(),
        eventName = eventName,
//        isEarned = isEarned,
//        dateEarned = dateEarned,
        pointsReward = pointsReward
    )
}



// -- Icon Mapper: String to Icon
fun getIconFromName(name: String): ImageVector {
    return when (name.lowercase()) {
        "park" -> Icons.Default.Park
        "shield" -> Icons.Default.Shield
        "waterdrop" -> Icons.Default.WaterDrop
        "wb_sunny", "wbsunny" -> Icons.Default.WbSunny
        "recycling" -> Icons.Default.Recycling
        "airplanemodeactive" -> Icons.Default.AirplanemodeActive
        else -> Icons.Default.Star // fallback icon
    }
}

// -- Icon Mapper: Icon to String
fun getIconName(icon: ImageVector): String {
    return when (icon) {
        Icons.Default.Park -> "park"
        Icons.Default.Shield -> "shield"
        Icons.Default.WaterDrop -> "waterdrop"
        Icons.Default.WbSunny -> "wbsunny"
        Icons.Default.Recycling -> "recycling"
        Icons.Default.AirplanemodeActive -> "airplanemodeactive"
        else -> "star"
    }
}

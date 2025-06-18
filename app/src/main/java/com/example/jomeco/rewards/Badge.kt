package com.example.jomeco.rewards



import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.jomeco.R

data class Badge(
    val id: String,
    val name: String,
    val description: String,
    val iconResId: Int,
    val color: Color,
    val eventName: String,
    val isEarned: Boolean = false,
    val dateEarned: String? = null,
    val pointsReward: Int
)


// Badge.kt (same file where your Badge class is)

fun getIconNameFromResId(resId: Int): String {
    return when (resId) {
        R.drawable.treeplanter -> "treeplanter"
        R.drawable.ecowarrior -> "ecowarrior"
        R.drawable.watersaver -> "watersaver"
        R.drawable.solarchampion -> "solarchampion"
        R.drawable.recyclinghero -> "recyclinghero"
        R.drawable.cleanairadvocate -> "cleanairadvocate"

        else -> ""
    }
}

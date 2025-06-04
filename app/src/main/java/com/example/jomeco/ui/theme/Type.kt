package com.example.jomeco.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.jomeco.R


val AnekOdia = FontFamily(
    Font(R.font.anekodia_semiexpanded_regular)
)

val Alef = FontFamily(
    Font(R.font.alef_regular)
)

val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Alef,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    displayLarge = TextStyle(
        fontFamily = AnekOdia,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp
    ),




    )
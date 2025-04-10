package com.jozefv.whatsnew.core.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jozefv.whatsnew.R

val DMSerif = FontFamily(
    Font(R.font.dm_serif_text_regular, weight = FontWeight.Normal, style = FontStyle.Normal),
    Font(R.font.dm_serif_text_italic, weight = FontWeight.Normal, style = FontStyle.Italic)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = DMSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = DMSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = DMSerif,
        fontWeight = FontWeight.Thin,
        fontSize = 10.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = DMSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = DMSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = DMSerif,
        fontWeight = FontWeight.Thin,
        fontSize = 12.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
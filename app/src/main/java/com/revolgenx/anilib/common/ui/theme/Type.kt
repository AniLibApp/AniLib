package com.revolgenx.anilib.common.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R


val rubikFonts = FontFamily(
    Font(R.font.rubik_regular),
    Font(R.font.rubik_medium, weight = FontWeight.Medium),
    Font(R.font.rubik_semi_bold, weight = FontWeight.SemiBold),
    Font(R.font.rubik_bold, weight = FontWeight.Bold),
)

val cabinFonts = FontFamily(
    Font(R.font.cabin_regular),
    Font(R.font.cabin_medium, weight = FontWeight.Medium),
    Font(R.font.cabin_semi_bold, weight = FontWeight.SemiBold),
)

val overpassFonts = FontFamily(
    Font(R.font.overpass_regular),
    Font(R.font.overpass_medium, weight = FontWeight.Medium),
    Font(R.font.overpass_semi_bold, weight = FontWeight.SemiBold),
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = overpassFonts,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelLarge = TextStyle(
        fontFamily = overpassFonts,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.2.sp
    ),
    titleLarge = TextStyle(
        fontFamily = overpassFonts,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.2.sp
    ),
    titleMedium = TextStyle(
        fontFamily = overpassFonts,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.2.sp
    ),
    titleSmall = TextStyle(
        fontFamily = overpassFonts,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
)




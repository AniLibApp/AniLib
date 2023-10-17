package com.revolgenx.anilib.common.ui.theme

import androidx.compose.material3.Typography
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
    Font(R.font.overpass_bold, weight = FontWeight.Bold),
)

// Set of Material typography styles to start with

val defaultTypography = Typography()

val Typography = Typography(
    displayMedium = defaultTypography.displayMedium.copy(
        fontFamily = overpassFonts
    ),
    bodyLarge = defaultTypography.bodyLarge.copy(
        fontFamily = overpassFonts,
        lineHeight = 20.sp
    ),
    bodyMedium = defaultTypography.bodyMedium.copy(
        fontFamily = overpassFonts,
        lineHeight = 18.sp
    ),
    labelLarge = defaultTypography.labelLarge.copy(
        fontFamily = overpassFonts,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.2.sp,
    ),
    titleLarge = defaultTypography.titleLarge.copy(
        fontFamily = overpassFonts,
        lineHeight = 26.sp,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium
    ),
    titleMedium = defaultTypography.titleMedium.copy(
        fontFamily = overpassFonts
    ),
    titleSmall = defaultTypography.titleSmall.copy(
        fontFamily = overpassFonts,
    ),
)

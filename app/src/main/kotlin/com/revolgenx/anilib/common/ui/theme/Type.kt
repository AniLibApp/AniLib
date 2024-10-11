package com.revolgenx.anilib.common.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R

val interFonts = FontFamily(
    Font(R.font.inter_regular),
    Font(R.font.inter_extralight, weight = FontWeight.ExtraLight),
    Font(R.font.inter_light, weight = FontWeight.Light),
    Font(R.font.inter_medium, weight = FontWeight.Medium),
    Font(R.font.inter_semibold, weight = FontWeight.SemiBold),
    Font(R.font.inter_bold, weight = FontWeight.Bold),
)

val defaultTypography = Typography()

val Typography = Typography(
    displayMedium = defaultTypography.displayMedium.copy(
        fontFamily = interFonts
    ),
    bodyLarge = defaultTypography.bodyLarge.copy(
        fontFamily = interFonts,
        lineHeight = 20.sp
    ),
    bodyMedium = defaultTypography.bodyMedium.copy(
        fontFamily = interFonts,
        lineHeight = 18.sp
    ),
    labelLarge = defaultTypography.labelLarge.copy(
        fontFamily = interFonts,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.2.sp,
    ),
    titleLarge = defaultTypography.titleLarge.copy(
        fontFamily = interFonts,
        lineHeight = 26.sp,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium
    ),
    titleMedium = defaultTypography.titleMedium.copy(
        fontFamily = interFonts
    ),
    titleSmall = defaultTypography.titleSmall.copy(
        fontFamily = interFonts,
    ),
)

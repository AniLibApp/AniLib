package com.revolgenx.anilib.common.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.ln

fun getColorDarkness(color: Color): Double {
    return 1 - (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue)
}

fun isColorDark(color: Color): Boolean {
    return getColorDarkness(color) >= 0.5
}

fun colorAtElevation(
    color: Color,
    tint: Color,
    elevation: Dp,
): Color {
    if (elevation == 0.dp) return color
    val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
    return tint.copy(alpha = alpha).compositeOver(color)
}

val Int.colorHex: String
    get()="#%08X".format(this)
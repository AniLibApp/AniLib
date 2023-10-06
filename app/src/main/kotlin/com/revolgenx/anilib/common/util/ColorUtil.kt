package com.revolgenx.anilib.common.util

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils.calculateContrast
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min


const val MAX_CONTRAST = 1.0f
const val CONTRAST_FACTOR = 1.5f
const val VISIBLE_CONTRAST = 0.45f

fun getColorDarkness(@ColorInt color: Int): Double {
    return 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
}

fun isColorDark(@ColorInt color: Int): Boolean {
    return getColorDarkness(color) >= 0.5
}

@ColorInt
fun getTintColor(@ColorInt color: Int): Int {
    return getContrastColor(color, color)
}

@ColorInt
fun getContrastColor(@ColorInt color: Int, @ColorInt contrastWith: Int): Int {
    return getContrastColor(color, contrastWith, VISIBLE_CONTRAST)
}

@ColorInt
fun getContrastColor(
    @ColorInt color: Int,
    @ColorInt contrastWith: Int,
    @FloatRange(from = 0.0, to = 1.0) visibleContrast: Float
): Int {
    return getContrastColor(color, contrastWith, visibleContrast, true)
}

@ColorInt
fun getContrastColor(
    @ColorInt color: Int, @ColorInt contrastWith: Int,
    @FloatRange(from = 0.0, to = 1.0) visibleContrast: Float, recursive: Boolean
): Int {
    val key = color.toString() + contrastWith + visibleContrast
    val contrast = calculateContrast(color, contrastWith).toFloat()
    if (contrast < visibleContrast) {
        val finalContrast = min(
            MAX_CONTRAST,
            visibleContrast.coerceAtLeast((visibleContrast - contrast) * CONTRAST_FACTOR)
        )
        val contrastColor = if (isColorDark(contrastWith)) {
            if (recursive && isColorDark(color)) getContrastColor(
                color,
                color,
                visibleContrast,
                false
            ) else getLighterColor(color, finalContrast)
        } else {
            if (recursive && !isColorDark(color)) getContrastColor(
                color,
                color,
                visibleContrast,
                false
            ) else getDarkerColor(color, finalContrast)
        }
        return contrastColor
    }
    return color
}

@ColorInt
fun getLighterColor(
    @ColorInt color: Int,
    @FloatRange(from = 0.0, to = 1.0) amount: Float, adjust: Boolean
): Int {
    var color = color
    if (adjust) {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        if (hsv[2] == 0f) {
            hsv[2] = min(1.0f, max(amount, VISIBLE_CONTRAST))
            color = Color.HSVToColor(Color.alpha(color), hsv)
        }
    }
    val alpha = (Color.alpha(color) + (255 - Color.alpha(color)) * amount).toInt()
    val red = (Color.red(color) + (255 - Color.red(color)) * amount).toInt()
    val green = (Color.green(color) + (255 - Color.green(color)) * amount).toInt()
    val blue = (Color.blue(color) + (255 - Color.blue(color)) * amount).toInt()
    return Color.argb(max(alpha, Color.alpha(color)), red, green, blue)
}

@ColorInt
fun getLighterColor(
    @ColorInt color: Int,
    @FloatRange(from = 0.0, to = 1.0) amount: Float
): Int {
    return getLighterColor(color, amount, true)
}

@ColorInt
fun getDarkerColor(
    @ColorInt color: Int,
    @FloatRange(from = 0.0, to = 1.0) amount: Float, adjust: Boolean
): Int {
    var color = color
    if (adjust) {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        if (hsv[2] == 1f) {
            hsv[2] = max(0f, min(amount, VISIBLE_CONTRAST))
            color = Color.HSVToColor(Color.alpha(color), hsv)
        }
    }
    val alpha = (Color.alpha(color) * (1.0 - amount)).toInt()
    val red = (Color.red(color) * (1.0 - amount)).toInt()
    val green = (Color.green(color) * (1.0 - amount)).toInt()
    val blue = (Color.blue(color) * (1.0 - amount)).toInt()
    return Color.argb(max(alpha, Color.alpha(color)), red, green, blue)
}

fun colorAtElevation(
    color: androidx.compose.ui.graphics.Color,
    tint: androidx.compose.ui.graphics.Color,
    elevation: Dp,
): androidx.compose.ui.graphics.Color {
    if (elevation == 0.dp) return color
    val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
    return tint.copy(alpha = alpha).compositeOver(color)
}

@ColorInt
fun getDarkerColor(
    @ColorInt color: Int,
    @FloatRange(from = 0.0, to = 1.0) amount: Float
): Int {
    return getDarkerColor(color, amount, true)
}

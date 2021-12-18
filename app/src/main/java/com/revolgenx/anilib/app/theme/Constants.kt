package com.revolgenx.anilib.app.theme

import android.graphics.Color
import androidx.annotation.ColorInt
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.theme.Theme

object Constants {

    @ColorInt
    val DEFAULT_BACKGROUND_COLOR_LIGHT = Color.parseColor("#f0f0f0")
    @ColorInt
    val DEFAULT_BACKGROUND_COLOR_DARK = Color.parseColor("#000000")

    @ColorInt
    val DEFAULT_SURFACE_COLOR_LIGHT = Color.parseColor("#E6E6E6")
    @ColorInt
    val DEFAULT_SURFACE_COLOR_DARK = Color.parseColor("#242424")

    @ColorInt
    val DEFAULT_ACCENT_COLOR_LIGHT = Color.parseColor("#02A4F8")
    @ColorInt
    val DEFAULT_ACCENT_COLOR_DARK = Color.parseColor("#02A4F8")

    const val DEFAULT_CORNER_RADIUS = 8
    const val DEFAULT_CARD_ELEVATION = 1
    const val DEFAULT_THEME_MODE = "0" //1 light 2 dark
    const val DEFAULT_LIGHT_THEME_MODE = "0" //DEFAULT
    const val DEFAULT_DARK_THEME_MODE = "0" //DEFAULT

    const val PREF_SETTINGS_THEME_MODE = "pref_settings_theme_mode"
    const val PREF_SETTINGS_LIGHT_THEME_MODE = "pref_settings_light_theme"
    const val PREF_SETTINGS_DARK_THEME_MODE = "pref_settings_dark_theme"

    const val PREF_SETTINGS_BACKGROUND_COLOR_LIGHT = "pref_settings_background_color_light"
    const val PREF_SETTINGS_BACKGROUND_COLOR_DARK = "pref_settings_background_color_dark"

    const val PREF_SETTINGS_SURFACE_COLOR_LIGHT = "pref_settings_surface_color_light"
    const val PREF_SETTINGS_SURFACE_COLOR_DARK = "pref_settings_surface_color_dark"

    const val PREF_SETTINGS_ACCENT_COLOR_LIGHT = "pref_settings_accent_color_light"
    const val PREF_SETTINGS_ACCENT_COLOR_DARK = "pref_settings_accent_color_dark"

    const val PREF_SETTINGS_CORNER_RADIUS = "pref_settings_corner_radius"
    const val PREF_SETTINGS_CARD_ELEVATION = "pref_settings_card_elevation"

}
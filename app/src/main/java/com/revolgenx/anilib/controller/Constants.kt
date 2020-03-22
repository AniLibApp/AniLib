package com.revolgenx.anilib.controller

import android.graphics.Color
import androidx.annotation.ColorInt
import com.pranavpandey.android.dynamic.theme.Theme

object Constants {
    /**
     * Default value for app theme color.
     *
     * `Auto` to use day and night themes.
     */
    @ColorInt
    const val APP_THEME_COLOR = Theme.AUTO

    /**
     * TODO: Default value for app theme day color.
     */
    @ColorInt
    val APP_THEME_DAY_COLOR = Color.parseColor("#f0f0f0")

    /**
     * TODO: Default value for app theme night color.
     */
    @ColorInt
    val APP_THEME_NIGHT_COLOR = Color.parseColor("#171E28")

    /**
     * TODO: Default value for app theme primary color.
     */
    @ColorInt
    val APP_THEME_COLOR_PRIMARY = Color.parseColor("#fafafa")

    /**
     * TODO: Default value for app theme accent color.
     */
    @ColorInt
    val APP_THEME_COLOR_ACCENT = Color.parseColor("#02A4F8")


    /**
     * Shared preferences key for first launch of the app.
     */
    const val PREF_FIRST_LAUNCH = "pref_first_launch"

    /**
     * Shared preferences key for app theme color.
     */
    const val PREF_SETTINGS_APP_THEME_COLOR = "pref_settings_app_theme_color"

    /**
     * Shared preferences key for app theme day color.
     */
    const val PREF_SETTINGS_APP_THEME_DAY_COLOR = "pref_settings_app_theme_day_color"

    /**
     * Shared preferences key for app theme night color.
     */
    const val PREF_SETTINGS_APP_THEME_NIGHT_COLOR = "pref_settings_app_theme_night_color"

    /**
     * Shared preferences key for app theme primary color.
     */
    const val PREF_SETTINGS_APP_THEME_COLOR_PRIMARY = "pref_settings_app_theme_color_primary"

    /**
     * Shared preferences key for app theme accent color.
     */
    const val PREF_SETTINGS_APP_THEME_COLOR_ACCENT = "pref_settings_app_theme_color_accent"

    /**
     * Shared preferences key for navigation bar theme.
     */
    const val PREF_SETTINGS_NAVIGATION_BAR_THEME = "pref_settings_navigation_bar_theme"

    /**
     * Shared preferences key for app shortcuts theme.
     */
    const val PREF_SETTINGS_APP_SHORTCUTS_THEME = "pref_settings_app_shortcuts_theme"

    /**
     * Shared preferences default value for first launch of the app.
     */
    const val PREF_FIRST_LAUNCH_DEFAULT = true

    /**
     * Shared preferences default value for app theme color.
     */
    const val PREF_SETTINGS_APP_THEME_COLOR_DEFAULT = APP_THEME_COLOR

    /**
     * Shared preferences default value for app theme day color.
     */
    @ColorInt
    val PREF_SETTINGS_APP_THEME_DAY_COLOR_DEFAULT = APP_THEME_DAY_COLOR

    /**
     * Shared preferences default value for app theme night color.
     */
    @ColorInt
    val PREF_SETTINGS_APP_THEME_NIGHT_COLOR_DEFAULT = APP_THEME_NIGHT_COLOR

    /**
     * Shared preferences default value for app theme primary color.
     */
    @ColorInt
    val PREF_SETTINGS_APP_THEME_COLOR_PRIMARY_DEFAULT = APP_THEME_COLOR_PRIMARY

    /**
     * Shared preferences default value for app theme accent color.
     */
    @ColorInt
    val PREF_SETTINGS_APP_THEME_COLOR_ACCENT_DEFAULT = APP_THEME_COLOR_ACCENT

    /**
     * Shared preferences default value for navigation bar theme.
     */
    const val PREF_SETTINGS_NAVIGATION_BAR_THEME_DEFAULT = false

    /**
     * Shared preferences default value for app shortcuts theme.
     */
    const val PREF_SETTINGS_APP_SHORTCUTS_THEME_DEFAULT = true
}
package com.revolgenx.anilib.app.theme


import androidx.annotation.ColorInt
import androidx.annotation.StyleRes
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils
import com.revolgenx.anilib.R


object ThemeController {

    val themeMode
        get() = DynamicPreferences.getInstance().load(
            Constants.PREF_SETTINGS_THEME_MODE,
            Constants.DEFAULT_THEME_MODE
        )

    val isThemeModeAuto
        get() = themeMode == "0"

    val isDarkMode
        get() = if (isThemeModeAuto)
            DynamicTheme.getInstance().isSystemNightMode
        else themeMode == "2"

    val lightThemeMode
        get() = DynamicPreferences.getInstance().load(
            Constants.PREF_SETTINGS_LIGHT_THEME_MODE,
            Constants.DEFAULT_LIGHT_THEME_MODE
        )

    val darkThemeMode
        get() = DynamicPreferences.getInstance().load(
            Constants.PREF_SETTINGS_DARK_THEME_MODE,
            Constants.DEFAULT_DARK_THEME_MODE
        )

    val isLightThemeInCustomMode
        get() = lightThemeMode == "1"

    val isDarkThemeInCustomMode
        get() = darkThemeMode == "1"


    val backgroundColorKey
        get() = if (isDarkMode) Constants.PREF_SETTINGS_BACKGROUND_COLOR_DARK else Constants.PREF_SETTINGS_BACKGROUND_COLOR_LIGHT
    val surfaceColorKey
        get() = if (isDarkMode) Constants.PREF_SETTINGS_SURFACE_COLOR_DARK else Constants.PREF_SETTINGS_SURFACE_COLOR_LIGHT
    val accentColorKey
        get() = if (isDarkMode) Constants.PREF_SETTINGS_ACCENT_COLOR_DARK else Constants.PREF_SETTINGS_ACCENT_COLOR_LIGHT

    val defaultBackgroundColor
        get() = if (isDarkMode) Constants.DEFAULT_BACKGROUND_COLOR_DARK else Constants.DEFAULT_BACKGROUND_COLOR_LIGHT
    val defaultSurfaceColor
        get() = if (isDarkMode) Constants.DEFAULT_SURFACE_COLOR_DARK else Constants.DEFAULT_SURFACE_COLOR_LIGHT
    val defaultAccentColor
        get() = if (isDarkMode) Constants.DEFAULT_ACCENT_COLOR_DARK else Constants.DEFAULT_ACCENT_COLOR_LIGHT
    private val defaultCornerRadius = Constants.DEFAULT_CORNER_RADIUS

    val backgroundColor: Int
        get() = DynamicPreferences.getInstance().load(
            backgroundColorKey,
            defaultBackgroundColor
        )

    val surfaceColor: Int
        get() = DynamicPreferences.getInstance().load(
            surfaceColorKey,
            defaultSurfaceColor
        )

    var accentColor: Int
        @ColorInt get() = DynamicPreferences.getInstance().load(
            accentColorKey,
            defaultAccentColor
        )
        set(value) {
            DynamicPreferences.getInstance()
                .save(accentColorKey, value)
        }

    val cornerRadius: Int
        get() = DynamicPreferences.getInstance().load(
            Constants.PREF_SETTINGS_CORNER_RADIUS,
            defaultCornerRadius
        )

    val appStyle: Int
        @StyleRes get() = if (DynamicColorUtils.isColorDark(backgroundColor))
            R.style.AppTheme
        else
            R.style.AppTheme_Dark

    val dynamicAppTheme: DynamicAppTheme
        get() = DynamicAppTheme()
            .setPrimaryColor(backgroundColor)
            .setAccentColor(accentColor)
            .setBackgroundColor(backgroundColor)
            .setSurfaceColor(surfaceColor)
            .setCornerRadiusDp(cornerRadius.toFloat())

    val elevaton
        get() = DynamicPreferences.getInstance().load(Constants.PREF_SETTINGS_CARD_ELEVATION, Constants.DEFAULT_CARD_ELEVATION)


    fun lightSurfaceColor(): Int {
        val surfaceColor = DynamicTheme.getInstance().get().surfaceColor
        return if (DynamicColorUtils.isColorDark(surfaceColor)) {
            surfaceColor
        } else {
            DynamicColorUtils.getLighterColor(surfaceColor, 0.4f)
        }
    }
}



package com.revolgenx.anilib.app.theme


import androidx.annotation.ColorInt
import androidx.annotation.StyleRes
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.theme.Theme
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils
import com.revolgenx.anilib.R

/**
 * Helper class to perform theme related operations.
 */
object ThemeController {

    /**
     * `true` if `auto` theme is selected.
     */
    val isAutoTheme: Boolean get() = appThemeColor == Theme.AUTO


    /**
     * Getter and Setter for the app theme color.
     */
    private var appThemeColor: Int
        get() = DynamicPreferences.getInstance().load(
            Constants.PREF_SETTINGS_APP_THEME_COLOR,
            Constants.PREF_SETTINGS_APP_THEME_COLOR_DEFAULT
        )
        set(@ColorInt color) = DynamicPreferences.getInstance().save(
            Constants.PREF_SETTINGS_APP_THEME_COLOR, color
        )

    /**
     * Getter and Setter for the app theme day color.
     */
    private var appThemeDayColor: Int
        @ColorInt get() = DynamicPreferences.getInstance().load(
            Constants.PREF_SETTINGS_APP_THEME_DAY_COLOR,
            Constants.PREF_SETTINGS_APP_THEME_DAY_COLOR_DEFAULT
        )
        set(@ColorInt color) = DynamicPreferences.getInstance().save(
            Constants.PREF_SETTINGS_APP_THEME_DAY_COLOR, color
        )

    /**
     * Getter and Setter for the app theme night color.
     */
    private var appThemeNightColor: Int
        @ColorInt get() = DynamicPreferences.getInstance().load(
            Constants.PREF_SETTINGS_APP_THEME_NIGHT_COLOR,
            Constants.PREF_SETTINGS_APP_THEME_NIGHT_COLOR_DEFAULT
        )
        set(@ColorInt color) = DynamicPreferences.getInstance().save(
            Constants.PREF_SETTINGS_APP_THEME_NIGHT_COLOR, color
        )

    /**
     * The app theme splash style according to the current settings.
     */
    val appStyle: Int
        @StyleRes get() = if (appThemeColor == Theme.AUTO) {
            getAppStyle(
                if (DynamicTheme.getInstance().isNight)
                    appThemeNightColor
                else
                    appThemeDayColor
            )
        } else {
            getAppStyle(appThemeColor)
        }

    /**
     * The background color according to the current settings.
     */
    private val backgroundColor: Int
        @ColorInt get() = if (appThemeColor == Theme.AUTO) {
            if (DynamicTheme.getInstance().isNight)
                appThemeNightColor
            else
                appThemeDayColor
        } else {
            appThemeColor
        }

    /**
     * Returns the app theme style according to the supplied color.
     *
     * @param color The color used for the background.
     *
     * @return The app theme style according to the supplied color.
     */
    @StyleRes
    private fun getAppStyle(@ColorInt color: Int): Int {
        return if (DynamicColorUtils.isColorDark(color))
            R.style.AppTheme
        else
            R.style.AppTheme_Dark
    }

    /**
     * Set the application theme according to the current settings.
     */
    fun setApplicationTheme() {
        DynamicTheme.getInstance().application
            .setPrimaryColor(AppController.instance.colorPrimaryApp)
            .setPrimaryColorDark(Theme.AUTO)
            .setAccentColor(AppController.instance.colorAccentApp)
            .setBackgroundColor(backgroundColor)
            .setSurfaceColor(Theme.AUTO).autoGenerateColors()
            .setCornerRadiusDp(10f);
    }

    /**
     * Set the local theme according to the current settings.
     */
    fun setLocalTheme() {
        DynamicTheme.getInstance().get()
            .setPrimaryColor(AppController.instance.colorPrimaryApp)
            .setPrimaryColorDark(Theme.AUTO)
            .setAccentColor(AppController.instance.colorAccentApp)
            .setBackgroundColor(backgroundColor)
            .setSurfaceColor(Theme.AUTO).autoGenerateColors()
            .setCornerRadiusDp(10f)
    }


    fun lightSurfaceColor(): Int {
        val surfaceColor = DynamicTheme.getInstance().get().surfaceColor
        return if (DynamicColorUtils.isColorDark(surfaceColor)) {
            surfaceColor
        } else {
            DynamicColorUtils.getLighterColor(surfaceColor, 0.4f)
        }
    }
}


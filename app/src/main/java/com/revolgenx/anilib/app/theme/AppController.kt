package com.revolgenx.anilib.app.theme

import android.app.Application
import androidx.annotation.ColorInt
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences

class AppController {

    /**
     * Application context used by this instance.
     */
    var context: Application? = null

    /**
     * Getter and Setter for the [Constants.PREF_FIRST_LAUNCH] shared preference.
     */
    var isFirstLaunch: Boolean
        get() = DynamicPreferences.getInstance().load(
            Constants.PREF_FIRST_LAUNCH,
            Constants.PREF_FIRST_LAUNCH_DEFAULT)
        set(firstLaunch) = DynamicPreferences.getInstance().save(
            Constants.PREF_FIRST_LAUNCH, firstLaunch)

    /**
     * The app theme primary color.
     */
    val colorPrimaryApp: Int
        @ColorInt get() = DynamicPreferences.getInstance().load(
            Constants.PREF_SETTINGS_APP_THEME_COLOR_PRIMARY,
            Constants.PREF_SETTINGS_APP_THEME_COLOR_PRIMARY_DEFAULT)

    /**
     * The app theme accent color.
     */
    val colorAccentApp: Int
        @ColorInt get() = DynamicPreferences.getInstance().load(
            Constants.PREF_SETTINGS_APP_THEME_COLOR_ACCENT,
            Constants.PREF_SETTINGS_APP_THEME_COLOR_ACCENT_DEFAULT)

    /**
     * true` to apply the navigation bar theme.
     */
    val isThemeNavigationBar: Boolean
        get() = DynamicPreferences.getInstance().load(
            Constants.PREF_SETTINGS_NAVIGATION_BAR_THEME,
            Constants.PREF_SETTINGS_NAVIGATION_BAR_THEME_DEFAULT)

    /**
     * true` to apply the app shortcuts theme.
     */
    val isThemeAppShortcuts: Boolean
        get() = DynamicPreferences.getInstance().load(
            Constants.PREF_SETTINGS_APP_SHORTCUTS_THEME,
            Constants.PREF_SETTINGS_APP_SHORTCUTS_THEME_DEFAULT)

    /**
     * Making default constructor private so that it cannot be initialized without a context.
     * Use [.initializeInstance] instead.
     */
    private constructor() {}

    /**
     * Default constructor to initialize this controller.
     */
    private constructor(context: Application) {
        this.context = context
    }

    companion object {

        /**
         * Singleton instance of [AppController].
         */
        private var sInstance: AppController? = null

        /**
         * Initialize this controller when application starts.
         * Must be initialize once.
         *
         * @param context The context to retrieve resources.
         */
        @Synchronized
        fun initializeInstance(context: Application?) {
            if (context == null) {
                throw NullPointerException("Context should not be null")
            }

            if (sInstance == null) {
                sInstance = AppController(context)
            }
        }

        /**
         * Get instance to access public methods.
         * Must be called before accessing methods.
         *
         * @return [.sInstance] Singleton [AppController] instance.
         */
        val instance: AppController
            @Synchronized get() {
                if (sInstance == null) {
                    throw IllegalStateException(AppController::class.java.simpleName
                            + " is not initialized, call initializeInstance(..) method first.")
                }

                return sInstance as AppController
            }
    }
}
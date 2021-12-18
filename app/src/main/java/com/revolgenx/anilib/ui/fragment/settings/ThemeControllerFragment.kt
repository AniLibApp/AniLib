package com.revolgenx.anilib.ui.fragment.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ArrayRes
import androidx.preference.PreferenceManager
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences
import com.pranavpandey.android.dynamic.support.setting.base.DynamicSpinnerPreference
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.AlThemeModel
import com.revolgenx.anilib.app.theme.AlThemes
import com.revolgenx.anilib.app.theme.Constants
import com.revolgenx.anilib.app.theme.ThemeController
import com.revolgenx.anilib.common.ui.fragment.BaseToolbarFragment
import com.revolgenx.anilib.databinding.ThemeControllerFragmentBinding
import com.revolgenx.anilib.ui.view.makeToast
import timber.log.Timber

class ThemeControllerFragment : BaseToolbarFragment<ThemeControllerFragmentBinding>(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override var titleRes: Int? = R.string.theme
    override val toolbarColorType: Int = Theme.ColorType.BACKGROUND
    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ThemeControllerFragmentBinding {
        return ThemeControllerFragmentBinding.inflate(inflater, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            themeModeSpinner.setEntries(R.array.theme_mode_entries)
            themeModeSpinner.setValues(R.array.theme_mode_values)
            lightThemeSpinner.setEntries(R.array.theme_entries)
            lightThemeSpinner.setValues(R.array.theme_values)
            darkThemeSpinner.setEntries(R.array.theme_entries)
            darkThemeSpinner.setValues(R.array.theme_values)
            backgroundColorPref.preferenceKey = ThemeController.backgroundColorKey
            surfaceColorPref.preferenceKey = ThemeController.surfaceColorKey
            accentColorPref.preferenceKey = ThemeController.accentColorKey
            backgroundColorPref.setColor(ThemeController.backgroundColor, false)
            surfaceColorPref.setColor(ThemeController.surfaceColor, false)
            accentColorPref.setColor(ThemeController.accentColor, false)

            val isDarkMode = ThemeController.isDarkMode
            if (isDarkMode) {
                disableCustomizableThemePref(ThemeController.isDarkThemeInCustomMode)
            } else {
                disableCustomizableThemePref(ThemeController.isLightThemeInCustomMode)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .registerOnSharedPreferenceChangeListener(this);
    }

    override fun onStop() {
        super.onStop()
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .unregisterOnSharedPreferenceChangeListener(this);
    }

    private fun ThemeControllerFragmentBinding.disableCustomizableThemePref(enable: Boolean = false) {
        backgroundColorPref.isEnabled = enable
        surfaceColorPref.isEnabled = enable
        accentColorPref.isEnabled = enable
        if(!enable){
            backgroundColorPref.setOnClickListener { makeToast(R.string.change_theme_to_custom) }
            surfaceColorPref.setOnClickListener { makeToast(R.string.change_theme_to_custom) }
            accentColorPref.setOnClickListener { makeToast(R.string.change_theme_to_custom) }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        super.onSharedPreferenceChanged(sharedPreferences, key)
        when (key) {
            Constants.PREF_SETTINGS_THEME_MODE -> {
                val isDarkMode = ThemeController.isDarkMode
                if (isDarkMode) {
                    binding.disableCustomizableThemePref(ThemeController.isDarkThemeInCustomMode)
                } else {
                    binding.disableCustomizableThemePref(ThemeController.isLightThemeInCustomMode)
                }
            }
            Constants.PREF_SETTINGS_LIGHT_THEME_MODE -> {
                val isDarkMode = ThemeController.isDarkMode
                val isCustomMode = ThemeController.isLightThemeInCustomMode
                if (!isDarkMode) {
                    binding.disableCustomizableThemePref(isCustomMode)
                }

                if (isCustomMode) return
                else {
                    when (val lightThemeMode = ThemeController.lightThemeMode) {
                        "0" -> {
                            saveDefaultLightTheme()
                        }
                        "1" -> {
                        }
                        else -> {
                            lightThemeMode?.toInt()?.let { mode ->
                                AlThemes.getThemes().firstOrNull { it.index == mode }?.let {
                                    saveAlTheme(isDarkMode, it)
                                }
                            }
                        }
                    }
                }
            }
            Constants.PREF_SETTINGS_DARK_THEME_MODE -> {
                val isDarkMode = ThemeController.isDarkMode
                val isCustomMode = ThemeController.isDarkThemeInCustomMode

                if (isDarkMode) {
                    binding.disableCustomizableThemePref(isCustomMode)
                }

                if (isCustomMode) return
                else {
                    when (val darkThemeMode = ThemeController.darkThemeMode) {
                        "0" -> {
                            saveDefaultDarkTheme()
                        }
                        "1" -> {
                        }
                        else -> {
                            darkThemeMode?.toInt()?.let { mode ->
                                AlThemes.getThemes().firstOrNull { it.index == mode }?.let {
                                    saveAlTheme(isDarkMode, it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun saveDefaultLightTheme() {
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .edit().putInt(
                Constants.PREF_SETTINGS_BACKGROUND_COLOR_LIGHT,
                Constants.DEFAULT_BACKGROUND_COLOR_LIGHT
            )
            .putInt(
                Constants.PREF_SETTINGS_SURFACE_COLOR_LIGHT,
                Constants.DEFAULT_SURFACE_COLOR_LIGHT
            )
            .putInt(
                Constants.PREF_SETTINGS_ACCENT_COLOR_LIGHT,
                Constants.DEFAULT_ACCENT_COLOR_LIGHT
            )
            .apply()
    }

    private fun saveDefaultDarkTheme() {
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .edit().putInt(
                Constants.PREF_SETTINGS_BACKGROUND_COLOR_DARK,
                Constants.DEFAULT_BACKGROUND_COLOR_DARK
            )
            .putInt(
                Constants.PREF_SETTINGS_SURFACE_COLOR_DARK,
                Constants.DEFAULT_SURFACE_COLOR_DARK
            )
            .putInt(
                Constants.PREF_SETTINGS_ACCENT_COLOR_DARK,
                Constants.DEFAULT_ACCENT_COLOR_DARK
            )
            .apply()
    }

    private fun saveAlTheme(isDarkMode: Boolean, model: AlThemeModel) {
        if (isDarkMode) {
            PreferenceManager.getDefaultSharedPreferences(requireContext())
                .edit().putInt(
                    Constants.PREF_SETTINGS_BACKGROUND_COLOR_DARK,
                    model.backgroundColor
                )
                .putInt(
                    Constants.PREF_SETTINGS_SURFACE_COLOR_DARK,
                    model.surfaceColor
                )
                .putInt(
                    Constants.PREF_SETTINGS_ACCENT_COLOR_DARK,
                    model.accentColor
                )
                .apply()
        } else {
            PreferenceManager.getDefaultSharedPreferences(requireContext())
                .edit().putInt(
                    Constants.PREF_SETTINGS_BACKGROUND_COLOR_LIGHT,
                    model.backgroundColor
                )
                .putInt(
                    Constants.PREF_SETTINGS_SURFACE_COLOR_LIGHT,
                    model.surfaceColor
                )
                .putInt(
                    Constants.PREF_SETTINGS_ACCENT_COLOR_LIGHT,
                    model.accentColor
                )
                .apply()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.apply {

        }
    }

    private fun DynamicSpinnerPreference.setEntries(@ArrayRes arrayId: Int) {
        setEntries(requireContext().resources.getStringArray(arrayId))
    }

    private fun DynamicSpinnerPreference.setValues(@ArrayRes arrayId: Int) {
        setValues(requireContext().resources.getStringArray(arrayId))
    }

}
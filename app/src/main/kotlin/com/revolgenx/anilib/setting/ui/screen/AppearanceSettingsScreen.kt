package com.revolgenx.anilib.setting.ui.screen

import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.revolgenx.anilib.setting.ui.component.ListPreferenceEntry
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.setting.ui.model.PreferenceModel
import com.revolgenx.anilib.setting.ui.viewmodel.AppearanceSettingsViewModel
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

object AppearanceSettingsScreen : ViewModelPreferencesScreen<AppearanceSettingsViewModel>() {
    override val titleRes: Int = I18nR.string.settings_appearance

    @Composable
    override fun getViewModel(): AppearanceSettingsViewModel = koinViewModel()

    @Composable
    override fun getPreferences(): List<PreferenceModel> {
        val themeDataStore = viewModel.themePreferencesDataStore
        val context = localContext()
        val themeEntries = remember {
            getThemeEntries(context)
        }
        return listOf(
            PreferenceModel.ListPreferenceModel(
                pref = themeDataStore.themeMode,
                title = stringResource(id = I18nR.string.settings_theme_dark_mode),
                entries = themeEntries
            )
        )
    }


    private fun getThemeEntries(context: Context): List<ListPreferenceEntry<Boolean>> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            listOf(
                ListPreferenceEntry(
                    title = context.getString(I18nR.string.settings_theme_dark_follow_system),
                    value = null
                ),
                ListPreferenceEntry(
                    title = context.getString(I18nR.string.settings_theme_dark_off),
                    value = false
                ),
                ListPreferenceEntry(
                    title = context.getString(I18nR.string.settings_theme_dark_on),
                    value = true
                ),
            )
        } else {
            listOf(
                ListPreferenceEntry(
                    title = context.getString(I18nR.string.settings_theme_dark_off),
                    value = false
                ),
                ListPreferenceEntry(
                    title = context.getString(I18nR.string.settings_theme_dark_on),
                    value = true
                ),
            )
        }
    }

}

package com.revolgenx.anilib.setting.ui.screen

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.revolgenx.anilib.common.data.store.PreferencesDataStore
import com.revolgenx.anilib.common.data.store.ThemeDataStore
import com.revolgenx.anilib.setting.ui.component.ListPreferenceEntry
import com.revolgenx.anilib.common.data.store.PreferenceDataModel
import com.revolgenx.anilib.setting.ui.model.PreferenceModel
import org.koin.androidx.compose.get
import anilib.i18n.R as I18nR

object AppearanceSettingsScreen : PreferenceScreen() {
    override val titleRes: Int = I18nR.string.settings_appearance

    @Composable
    override fun Content() {
        super.Content()
    }

    @Composable
    override fun getPreferences(): List<PreferenceModel> {
        val dataStore: PreferencesDataStore = get()
        return listOf(
            PreferenceModel.ListPreferenceModel(
                PreferenceDataModel(dataStore = dataStore, ThemeDataStore.themeModeKey),
                title = stringResource(id = I18nR.string.settings_theme_dark_mode),
                entries = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    listOf(
                        ListPreferenceEntry(
                            title = stringResource(id = I18nR.string.settings_theme_dark_follow_system),
                            value = null
                        ),
                        ListPreferenceEntry(
                            title = stringResource(id = I18nR.string.settings_theme_dark_off),
                            value = false
                        ),
                        ListPreferenceEntry(
                            title = stringResource(id = I18nR.string.settings_theme_dark_on),
                            value = true
                        ),
                    )
                } else {
                    listOf(
                        ListPreferenceEntry(
                            title = stringResource(id = I18nR.string.settings_theme_dark_off),
                            value = false
                        ),
                        ListPreferenceEntry(
                            title = stringResource(id = I18nR.string.settings_theme_dark_on),
                            value = true
                        ),
                    )
                }
            )
        )
    }

}

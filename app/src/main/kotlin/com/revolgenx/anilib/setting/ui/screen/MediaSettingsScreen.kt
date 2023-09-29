package com.revolgenx.anilib.setting.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.media.ui.model.MediaTitleModel
import com.revolgenx.anilib.setting.ui.component.ListPreferenceEntry
import com.revolgenx.anilib.setting.ui.model.PreferenceModel
import com.revolgenx.anilib.setting.ui.viewmodel.MediaSettingsViewModel
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

object MediaSettingsScreen : PreferenceScreen() {
    override val titleRes: Int = I18nR.string.settings_anime_and_manga

    @Composable
    override fun getPreferences(): List<PreferenceModel> {
        val mediaSettingsViewModel: MediaSettingsViewModel = koinViewModel()
        val authDataStore = mediaSettingsViewModel.authDataStore
        val mediaSettingsDataStore = mediaSettingsViewModel.mediaSettingsDataStore
        val isLoggedIn = authDataStore.isLoggedIn()
        val mediaTitleTypeState = mediaSettingsDataStore.mediaTitleType.collectAsState()

        val context = localContext()

        val prefList = mutableListOf<PreferenceModel>()
        val mediaTitleList = remember {
            listOf(
                ListPreferenceEntry(
                    title = context.getString(I18nR.string.romaji),
                    value = MediaTitleModel.type_romaji
                ),
                ListPreferenceEntry(
                    title = context.getString(I18nR.string.english),
                    value = MediaTitleModel.type_english
                ),
                ListPreferenceEntry(
                    title = context.getString(I18nR.string._native),
                    value = MediaTitleModel.type_native
                )
            )
        }

        val titleLanguagePref = if (isLoggedIn) {
            PreferenceModel.ListPreferenceModel(
                title = stringResource(id = I18nR.string.settings_title_language),
                prefState = mediaTitleTypeState,
                entries = mediaTitleList,
                onValueChanged = {
                    mediaSettingsDataStore.mediaTitleType.set(it)
                    true
                }
            )
        } else {
            PreferenceModel.ListPreferenceModel(
                pref = mediaSettingsDataStore.mediaTitleType,
                title = stringResource(id = I18nR.string.settings_title_language),
                entries = mediaTitleList
            )
        }

        prefList.add(titleLanguagePref)

        if (isLoggedIn) {
            prefList.add(PreferenceModel.ListPreferenceModel(
                title = stringResource(id = I18nR.string.settings_activity_merge_time),
                prefState = mediaTitleTypeState,
                entries = mediaTitleList,
                onValueChanged = {
                    mediaSettingsDataStore.mediaTitleType.set(it)
                    true
                }
            ))
            prefList.add(
                PreferenceModel.SwitchPreference(
                    pref = mediaSettingsDataStore.airingAnimeNotification,
                    title = stringResource(id = I18nR.string.settings_airing_anime_notifications),
                )
            )
            prefList.add(
                PreferenceModel.SwitchPreference(
                    pref = mediaSettingsDataStore.showAdultContent,
                    title = stringResource(id = I18nR.string.settings_adult_content),
                )
            )

        }

        return prefList
    }

}
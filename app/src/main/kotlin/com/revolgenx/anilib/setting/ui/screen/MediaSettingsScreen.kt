package com.revolgenx.anilib.setting.ui.screen

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.revolgenx.anilib.common.data.state.ResourceState
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.localSnackbarHostState
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.common.ShowIfLoggedIn
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcSave
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.setting.data.store.activityMergeTimePrefEntry
import com.revolgenx.anilib.setting.data.store.mediaTitlePrefEntry
import com.revolgenx.anilib.setting.ui.component.ListPreferenceItem
import com.revolgenx.anilib.setting.ui.component.SwitchPreferenceItem
import com.revolgenx.anilib.setting.ui.model.PreferenceModel
import com.revolgenx.anilib.setting.ui.viewmodel.MediaSettingsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

class MediaSettingsScreen : PreferencesScreen() {
    override val titleRes: Int = I18nR.string.settings_anime_and_manga


    @Composable
    override fun SaveAction() {
        val viewModel: MediaSettingsViewModel = koinViewModel()
        ShowIfLoggedIn {
            ActionMenu(
                icon = AppIcons.IcSave
            ) {
                viewModel.save()
            }
        }
    }

    @Composable
    override fun PreferencesContent() {
        val viewModel: MediaSettingsViewModel = koinViewModel()
        val authDataStore = viewModel.authPreferencesDataStore
        val isLoggedIn = authDataStore.isLoggedIn()

        if (isLoggedIn) {
            LaunchedEffect(viewModel) {
                viewModel.getResource()
            }
            ResourceScreen(viewModel = viewModel) {
                SaveResourceState(viewModel)
                MediaSettingsScreenContent(viewModel, isLoggedIn = isLoggedIn)
            }
        } else {
            MediaSettingsScreenContent(viewModel, isLoggedIn = isLoggedIn)
        }
    }

    @Composable
    private fun MediaSettingsScreenContent(viewModel: MediaSettingsViewModel, isLoggedIn: Boolean) {
        val context = localContext()
        val scope = rememberCoroutineScope()
        val resourceValue = viewModel.resource.value?.stateValue
        val mediaSettingsDataStore = viewModel.mediaSettingsPreferencesDataStore


        val mediaTitleList = remember {
            mediaTitlePrefEntry(context)
        }
        if (isLoggedIn) {
            ListPreferenceItem(
                value = resourceValue?.titleLanguage?.value,
                title = stringResource(id = I18nR.string.settings_title_language),
                entries = mediaTitleList
            ) {
                resourceValue?.titleLanguage?.value = it!!
            }

            val activityMergeList = remember {
                activityMergeTimePrefEntry(context)
            }

            ListPreferenceItem(
                value = resourceValue?.activityMergeTime?.intValue,
                title = stringResource(id = I18nR.string.settings_activity_merge_time),
                entries = activityMergeList
            ) {
                resourceValue?.activityMergeTime?.intValue = it!!
            }

            SwitchPreferenceItem(
                checked = resourceValue?.airingNotifications?.value == true,
                title = stringResource(id = I18nR.string.settings_airing_anime_notifications),
            ) {
                resourceValue?.airingNotifications?.value = it
            }

            if (resourceValue?.displayAdultContent?.value == true) {
                SwitchPreferenceItem(
                    checked = mediaSettingsDataStore.displayAdultContent.collectAsState().value == true,
                    title = stringResource(id = I18nR.string.settings_adult_content),
                    subtitle = stringResource(id = I18nR.string.show_adult_content_desc)
                ) {
                    scope.launch {
                        mediaSettingsDataStore.displayAdultContent.set(it)
                    }
                }
            }
        } else {
            ListPreferenceItem(
                value = mediaSettingsDataStore.mediaTitleType.collectAsState().value,
                title = stringResource(id = I18nR.string.settings_title_language),
                entries = mediaTitleList
            ) {
                scope.launch {
                    mediaSettingsDataStore.mediaTitleType.set(it)
                }
            }
        }

        val prefList = mutableListOf<PreferenceModel>()

        if (isLoggedIn) {
            val activityMergeList = remember {
                activityMergeTimePrefEntry(context)
            }
            prefList.add(PreferenceModel.ListPreferenceModel(
                title = stringResource(id = I18nR.string.settings_activity_merge_time),
                prefState = resourceValue?.activityMergeTime,
                entries = activityMergeList,
                onValueChanged = {
                    resourceValue?.activityMergeTime?.intValue = it!!
                    true
                }
            ))
            prefList.add(
                PreferenceModel.SwitchPreference(
                    prefState = resourceValue?.airingNotifications,
                    title = stringResource(id = I18nR.string.settings_airing_anime_notifications),
                ) {
                    resourceValue?.airingNotifications?.value = it
                    true
                }
            )

            if (resourceValue?.displayAdultContent?.value == true) {
                prefList.add(
                    PreferenceModel.SwitchPreference(
                        pref = mediaSettingsDataStore.displayAdultContent,
                        title = stringResource(id = I18nR.string.settings_adult_content),
                        subtitle = stringResource(id = I18nR.string.show_adult_content_desc)
                    )
                )
            }
        }
    }


    @Composable
    private fun SaveResourceState(viewModel: MediaSettingsViewModel) {
        val snackbar = localSnackbarHostState()
        when (viewModel.saveResource) {
            is ResourceState.Error -> {
                val failedToSave = stringResource(id = I18nR.string.failed_to_save)
                val retry = stringResource(id = I18nR.string.retry)
                LaunchedEffect(viewModel) {
                    when (snackbar.showSnackbar(
                        failedToSave, retry, duration = SnackbarDuration.Long
                    )) {
                        SnackbarResult.Dismissed -> {
                            viewModel.saveResource = null
                        }

                        SnackbarResult.ActionPerformed -> {
                            viewModel.save()
                        }
                    }
                }
            }

            else -> {}
        }
    }

}
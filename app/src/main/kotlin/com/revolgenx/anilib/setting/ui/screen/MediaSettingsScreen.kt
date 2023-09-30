package com.revolgenx.anilib.setting.ui.screen

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import com.revolgenx.anilib.setting.ui.model.PreferenceModel
import com.revolgenx.anilib.setting.ui.viewmodel.MediaSettingsViewModel
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

class MediaSettingsScreen : ViewModelPreferencesScreen<MediaSettingsViewModel>() {
    override val titleRes: Int = I18nR.string.settings_anime_and_manga

    @Composable
    override fun getViewModel(): MediaSettingsViewModel = koinViewModel()

    @Composable
    override fun SaveAction() {
        ShowIfLoggedIn {
            ActionMenu(
                icon = AppIcons.IcSave
            ) {
                viewModel.save()
            }
        }
    }

    @Composable
    override fun PreferenceContent() {
        ShowIfLoggedIn(
            orElse = {
                super.PreferenceContent()
            }
        ) {
            LaunchedEffect(viewModel) {
                viewModel.getResource()
            }
            ResourceScreen(viewModel = viewModel) {
                SaveResourceState(viewModel)
                super.PreferenceContent()
            }
        }
    }


    @Composable
    private fun SaveResourceState(viewModel: MediaSettingsViewModel) {
        val snackbar = localSnackbarHostState()
        when (viewModel.saveResource.value) {
            is ResourceState.Error -> {
                val failedToSave = stringResource(id = I18nR.string.failed_to_save)
                val retry = stringResource(id = I18nR.string.retry)
                LaunchedEffect(viewModel) {
                    when (snackbar.showSnackbar(
                        failedToSave, retry, duration = SnackbarDuration.Long
                    )) {
                        SnackbarResult.Dismissed -> {
                            viewModel.saveResource.value = null
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


    @Composable
    override fun getPreferences(): List<PreferenceModel> {
        val context = localContext()
        val viewModel: MediaSettingsViewModel = getViewModel()
        val resourceValue = viewModel.resource.value?.stateValue

        val authDataStore = viewModel.authDataStore
        val mediaSettingsDataStore = viewModel.mediaSettingsDataStore

        val isLoggedIn = authDataStore.isLoggedIn()

        val prefList = mutableListOf<PreferenceModel>()
        val mediaTitleList = remember {
            mediaTitlePrefEntry(context)
        }

        val titleLanguagePref = if (isLoggedIn) {
            PreferenceModel.ListPreferenceModel(
                title = stringResource(id = I18nR.string.settings_title_language),
                prefState = resourceValue?.titleLanguage,
                entries = mediaTitleList,
                onValueChanged = {
                    resourceValue?.titleLanguage?.value = it!!
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
                    )
                )
            }
        }

        return prefList
    }

}
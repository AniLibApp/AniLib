package com.revolgenx.anilib.setting.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.revolgenx.anilib.common.data.store.activityMergeTimePrefEntry
import com.revolgenx.anilib.common.data.store.mediaTitlePrefEntry
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.common.ShowIfLoggedIn
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcSave
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import com.revolgenx.anilib.setting.ui.component.ListPreferenceItem
import com.revolgenx.anilib.setting.ui.component.SwitchPreferenceItem
import com.revolgenx.anilib.setting.ui.viewmodel.MediaSettingsViewModel
import com.revolgenx.anilib.user.ui.model.getUserTitleLanguage
import com.revolgenx.anilib.user.ui.model.toMediaTitleType
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

object MediaSettingsScreen : AndroidScreen() {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel: MediaSettingsViewModel = koinViewModel()

        ScreenScaffold(
            title = stringResource(id = I18nR.string.settings_anime_and_manga),
            actions = {
                ShowIfLoggedIn {
                    ActionMenu(
                        icon = AppIcons.IcSave
                    ) {
                        viewModel.save()
                    }
                }
            },
            contentWindowInsets = horizontalBottomWindowInsets()
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                val authDataStore = viewModel.appPreferencesDataStore
                val isLoggedIn = authDataStore.isLoggedIn()

                if (isLoggedIn) {
                    LaunchedEffect(viewModel) {
                        viewModel.getResource()
                    }
                    ResourceScreen(viewModel = viewModel) {
                        MediaSettingsScreenContent(viewModel, isLoggedIn = isLoggedIn)
                    }
                } else {
                    MediaSettingsScreenContent(viewModel, isLoggedIn = isLoggedIn)
                }
            }
        }
    }
}


@Composable
private fun MediaSettingsScreenContent(viewModel: MediaSettingsViewModel, isLoggedIn: Boolean) {
    val context = localContext()
    val scope = rememberCoroutineScope()
    val mediaSettingsDataStore = viewModel.mediaSettingsPreferencesDataStore

    val mediaTitleList = remember {
        mediaTitlePrefEntry(context)
    }

    if (isLoggedIn) {
        val userOptionsState = viewModel.getData()?.options ?: return
        val userOptions = userOptionsState.value

        ListPreferenceItem(
            value = userOptions.titleLanguage.toMediaTitleType(),
            title = stringResource(id = I18nR.string.settings_title_language),
            entries = mediaTitleList
        ) {
            userOptionsState.value = userOptions.copy(titleLanguage = getUserTitleLanguage(it))
        }

        val activityMergeList = remember {
            activityMergeTimePrefEntry(context)
        }

        ListPreferenceItem(
            value = userOptions.activityMergeTime,
            title = stringResource(id = I18nR.string.settings_activity_merge_time),
            entries = activityMergeList
        ) {
            userOptionsState.value = userOptions.copy(activityMergeTime = it!!)
        }

        SwitchPreferenceItem(
            checked = userOptions.airingNotifications,
            title = stringResource(id = I18nR.string.settings_airing_anime_notifications),
        ) {
            userOptionsState.value = userOptions.copy(airingNotifications = it)
        }

        if (userOptions.displayAdultContent) {
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
}
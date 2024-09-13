package com.revolgenx.anilib.setting.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import anilib.i18n.R
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ui.component.menu.SortMenuItem
import com.revolgenx.anilib.common.ui.component.menu.SortOrder
import com.revolgenx.anilib.common.ui.component.menu.SortSelectMenu
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.composition.LocalUserState
import com.revolgenx.anilib.common.ui.composition.localUser
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import com.revolgenx.anilib.setting.ui.component.SwitchPreferenceItem
import com.revolgenx.anilib.setting.ui.viewmodel.SettingsViewModel
import com.revolgenx.anilib.type.AiringSort
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

object WidgetSettingsScreen : AndroidScreen() {

    @Composable
    override fun Content() {
        WidgetSettingsScreenContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WidgetSettingsScreenContent() {
    val scope = rememberCoroutineScope()
    val user = localUser()

    val settingsViewModel: SettingsViewModel = koinViewModel()
    val appPreferencesDataStore = settingsViewModel.appPreferencesDataStore


    ScreenScaffold(
        title = stringResource(id = R.string.widget),
        actions = {},
        contentWindowInsets = horizontalBottomWindowInsets()
    ) {
        Column {
            val widgetIncludeAlreadyAiredPref = appPreferencesDataStore.widgetIncludeAlreadyAired
            val includeAlreadyAired = widgetIncludeAlreadyAiredPref.collectAsState()

            SwitchPreferenceItem(
                title = stringResource(id = R.string.widget_already_aired),
                subtitle = stringResource(id = R.string.widget_include_already_aired_desc),
                checked = includeAlreadyAired.value!!
            ) {
                scope.launch {
                    widgetIncludeAlreadyAiredPref.set(it)
                }
            }

            if (user.isLoggedIn) {
                val widgetOpenListEditor = appPreferencesDataStore.widgetOpenListEditor
                val widgetOnlyPlanning = appPreferencesDataStore.widgetOnlyPlanning
                val widgetOnlyWatching = appPreferencesDataStore.widgetOnlyWatching

                val openListEditor = widgetOpenListEditor.collectAsState()
                val onlyPlanning = widgetOnlyPlanning.collectAsState()
                val onlyWatching = widgetOnlyWatching.collectAsState()

                SwitchPreferenceItem(
                    title = stringResource(id = R.string.widget_open_list_editor),
                    subtitle = stringResource(id = R.string.widget_open_list_editor_desc),
                    checked = openListEditor.value!!
                ) {
                    scope.launch {
                        widgetOpenListEditor.set(it)
                    }
                }


                SwitchPreferenceItem(
                    title = stringResource(id = R.string.widget_watching_list),
                    subtitle = stringResource(id = R.string.widget_watching_list_desc),
                    checked = onlyWatching.value!!
                ) {
                    scope.launch {
                        widgetOnlyWatching.set(it)
                    }
                }


                SwitchPreferenceItem(
                    title = stringResource(id = R.string.widget_planning_list),
                    subtitle = stringResource(id = R.string.widget_planning_list_desc),
                    checked = onlyPlanning.value!!
                ) {
                    scope.launch {
                        widgetOnlyPlanning.set(it)
                    }
                }


            }

            val widgetAiringSortPref = appPreferencesDataStore.widgetAiringSort
            val airingSort = widgetAiringSortPref.collectAsState()

            Box (
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            ){
                AiringSortMenu(AiringSort.entries[airingSort.value!!]) {
                    scope.launch {
                        widgetAiringSortPref.set(it.ordinal)
                    }
                }
            }

        }

    }
}


@Composable
private fun AiringSortMenu(airingSort: AiringSort, onSortSelected: (sort: AiringSort) -> Unit) {
    val selectedSort = (airingSort.ordinal + 1) / 2
    val isDesc = airingSort.rawValue.endsWith("_DESC")
    val selectedSortIndex = if (isDesc) selectedSort - 1 else selectedSort
    val selectedSortOrder = if (isDesc) SortOrder.DESC else SortOrder.ASC
    val sortMenus =
        stringArrayResource(id = com.revolgenx.anilib.R.array.airing_sort).mapIndexed { index, s ->
            SortMenuItem(
                s,
                if (index == selectedSortIndex) selectedSortOrder else SortOrder.NONE
            )
        }

    SortSelectMenu(
        label = stringResource(id = R.string.sort),
        entries = sortMenus,
        allowNone = false
    ) { index, selectedItem ->
        selectedItem ?: return@SortSelectMenu
        val order = selectedItem.order
        val airingIndex = index * 2
        val selectedAiringSort =
            AiringSort.entries[if (order == SortOrder.DESC) airingIndex + 1 else airingIndex]
        onSortSelected(selectedAiringSort)
    }
}
package com.revolgenx.anilib.setting.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.updateAll
import anilib.i18n.R
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ui.color.ColorPickerDialog
import com.revolgenx.anilib.common.ui.component.menu.SortMenuItem
import com.revolgenx.anilib.common.ui.component.menu.SortOrder
import com.revolgenx.anilib.common.ui.component.menu.SortSelectMenu
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.composition.localUser
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import com.revolgenx.anilib.common.ui.theme.surfaceColors
import com.revolgenx.anilib.common.util.OnClickWithValue
import com.revolgenx.anilib.setting.ui.component.GroupPreferenceItem
import com.revolgenx.anilib.setting.ui.component.HeaderPreferenceItem
import com.revolgenx.anilib.setting.ui.component.PrefsHorizontalPadding
import com.revolgenx.anilib.setting.ui.component.PrefsVerticalPadding
import com.revolgenx.anilib.setting.ui.component.SwitchPreferenceItem
import com.revolgenx.anilib.setting.ui.component.TextPreferenceItem
import com.revolgenx.anilib.setting.ui.viewmodel.WidgetSettingsViewModel
import com.revolgenx.anilib.type.AiringSort
import com.revolgenx.anilib.widget.ui.AiringScheduleWidget
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
    val context = localContext()

    val widgetSettingsViewModel: WidgetSettingsViewModel = koinViewModel()
    val appPreferencesDataStore = widgetSettingsViewModel.appPreferencesDataStore
    val widgetThemeDataStore = widgetSettingsViewModel.widgetThemeDataStore

    ScreenScaffold(
        title = stringResource(id = R.string.widget),
        actions = {},
        contentWindowInsets = horizontalBottomWindowInsets()
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            GroupPreferenceItem(title = stringResource(id = R.string.settings_appearance)) {
                val widgetBackgroundSameAsAppPref =
                    appPreferencesDataStore.widgetBackgroundSameAsApp
                val widgetBackgroundSameAsApp = widgetBackgroundSameAsAppPref.collectAsState()

                val widgetBackgroundSame = widgetBackgroundSameAsApp.value!!
                SwitchPreferenceItem(
                    title = stringResource(id = R.string.widget_same_background_as_app),
                    subtitle = stringResource(id = R.string.widget_same_background_as_app_desc),
                    checked = widgetBackgroundSame
                ) {
                    scope.launch {
                        if(!widgetBackgroundSame && it){
                            widgetSettingsViewModel.resetBackground(context)
                        }
                        widgetBackgroundSameAsAppPref.set(it)
                        AiringScheduleWidget().updateAll(context = context)
                    }
                }

                val widgetTheme = widgetThemeDataStore.collectAsState().value

                ColorGroupContent(
                    color = widgetTheme.background,
                    title = stringResource(id = R.string.background),
                    subtitle = stringResource(id = R.string.widget_background_color),
                    enabled = !widgetBackgroundSame,
                    colorsInt = surfaceColors
                ) {
                    widgetSettingsViewModel.setBackground(it, context)
                }

                HeaderPreferenceItem(title = stringResource(id = R.string.background_transparency)) {
                    val widgetBackground = Color(widgetTheme.background)
                    val alpha = widgetBackground.alpha.takeIf { it >= 0.6f } ?: 0.6f
                    val alphaState = remember(alpha){
                        mutableFloatStateOf(alpha)
                    }
                    Slider(
                        modifier = Modifier.padding(horizontal = PrefsHorizontalPadding, vertical = PrefsVerticalPadding),
                        value = alphaState.floatValue,
                        steps = 6,
                        onValueChange = { range ->
                            alphaState.floatValue = range
                        },
                        valueRange = 0.6f..1f,
                        onValueChangeFinished = {
                            widgetSettingsViewModel.setBackground(widgetBackground.copy(alpha = alphaState.floatValue).toArgb(), context)
                        }
                    )
                }
            }

            GroupPreferenceItem(title = stringResource(id = R.string.filter)) {
                val widgetIncludeAlreadyAiredPref =
                    appPreferencesDataStore.widgetIncludeAlreadyAired
                val includeAlreadyAired = widgetIncludeAlreadyAiredPref.collectAsState()

                SwitchPreferenceItem(
                    title = stringResource(id = R.string.widget_already_aired),
                    subtitle = stringResource(id = R.string.widget_include_already_aired_desc),
                    checked = includeAlreadyAired.value!!
                ) {
                    scope.launch {
                        widgetIncludeAlreadyAiredPref.set(it)
                        widgetSettingsViewModel.updateWidget(context)
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
                            widgetSettingsViewModel.updateWidget(context)
                        }
                    }


                    SwitchPreferenceItem(
                        title = stringResource(id = R.string.widget_watching_list),
                        subtitle = stringResource(id = R.string.widget_watching_list_desc),
                        checked = onlyWatching.value!!
                    ) {
                        scope.launch {
                            widgetOnlyWatching.set(it)
                            widgetSettingsViewModel.updateWidget(context)
                        }
                    }


                    SwitchPreferenceItem(
                        title = stringResource(id = R.string.widget_planning_list),
                        subtitle = stringResource(id = R.string.widget_planning_list_desc),
                        checked = onlyPlanning.value!!
                    ) {
                        scope.launch {
                            widgetOnlyPlanning.set(it)
                            widgetSettingsViewModel.updateWidget(context)
                        }
                    }


                }

                val widgetAiringSortPref = appPreferencesDataStore.widgetAiringSort
                val airingSort = widgetAiringSortPref.collectAsState()

                Box(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    AiringSortMenu(AiringSort.entries[airingSort.value!!]) {
                        scope.launch {
                            widgetAiringSortPref.set(it.ordinal)
                            widgetSettingsViewModel.updateWidget(context)
                        }
                    }
                }
            }


        }

    }


}


@Composable
private fun ColorGroupContent(
    color: Int,
    title: String,
    subtitle: String,
    enabled: Boolean,
    colorsInt: List<Int> = listOf(),
    onColorSelected: OnClickWithValue<Int>
) {

    val openColorPickerDialog = remember {
        mutableStateOf(false)
    }

    ColorPickerDialog(
        openDialog = openColorPickerDialog,
        title = title,
        selectedColor = color,
        colorsInt = colorsInt.toTypedArray(),
        onColorSelected = onColorSelected
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.5f)
            .clickable(enabled = enabled) {
                openColorPickerDialog.value = true
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextPreferenceItem(
            modifier = Modifier.weight(1f),
            title = title,
            subtitle = subtitle
        )
        Box(
            modifier = Modifier
                .padding(4.dp)
                .size(48.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = CircleShape
                )
                .clip(RoundedCornerShape(100.dp))
                .background(Color(color))
        )

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
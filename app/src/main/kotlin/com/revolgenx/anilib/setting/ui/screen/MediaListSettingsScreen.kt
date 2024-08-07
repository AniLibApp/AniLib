package com.revolgenx.anilib.setting.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import anilib.i18n.R
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.chip.ClearAssistChip
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcPlus
import com.revolgenx.anilib.common.ui.icons.appicon.IcSave
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import com.revolgenx.anilib.common.ui.theme.defaultTheme
import com.revolgenx.anilib.setting.ui.component.GroupPreferenceItem
import com.revolgenx.anilib.setting.ui.component.ListPreferenceEntry
import com.revolgenx.anilib.setting.ui.component.ListPreferenceItem
import com.revolgenx.anilib.setting.ui.component.SwitchPreferenceItem
import com.revolgenx.anilib.setting.ui.model.MediaListSettingsModel
import com.revolgenx.anilib.setting.ui.model.toModel
import com.revolgenx.anilib.setting.ui.viewmodel.MediaListSettingsViewModel
import com.revolgenx.anilib.type.ScoreFormat
import com.revolgenx.anilib.user.ui.model.MediaListOptionModel
import com.revolgenx.anilib.user.ui.model.RowOrder
import org.koin.androidx.compose.koinViewModel

object MediaListSettingsScreen : AndroidScreen() {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel: MediaListSettingsViewModel = koinViewModel()

        ScreenScaffold(
            title = stringResource(id = R.string.settings_lists), actions = {
                ActionMenu(
                    icon = AppIcons.IcSave
                ) {
                    viewModel.save()
                }
            }, contentWindowInsets = horizontalBottomWindowInsets()
        ) {
            LaunchedEffect(viewModel) {
                viewModel.getResource()
            }
            ResourceScreen(viewModel = viewModel) {
                val mediaListSettingsModel = viewModel.getData()
                MediaListSettingsScreenContent(mediaListSettingsModel)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MediaListSettingsScreenContent(mediaListSettingsModel: MediaListSettingsModel?) {
    val context = localContext()
    val listSettingsModel = mediaListSettingsModel ?: return

    Column(
        modifier = Modifier
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {
        val scoringEntries = remember {
            listOf(
                ListPreferenceEntry(
                    context.getString(R.string.settings_list_100_point), ScoreFormat.POINT_100
                ),
                ListPreferenceEntry(
                    context.getString(R.string.settings_list_10_point_decimal),
                    ScoreFormat.POINT_10_DECIMAL
                ),
                ListPreferenceEntry(
                    context.getString(R.string.settings_list_10_point), ScoreFormat.POINT_10
                ),
                ListPreferenceEntry(
                    context.getString(R.string.settings_list_5_star), ScoreFormat.POINT_5
                ),
                ListPreferenceEntry(
                    context.getString(R.string.settings_list_3_star), ScoreFormat.POINT_3
                ),
            )
        }

        ListPreferenceItem(
            value = listSettingsModel.scoreFormat.value,
            title = stringResource(id = R.string.settings_scoring_system),
            entries = scoringEntries
        ) {
            listSettingsModel.scoreFormat.value = it!!
        }

        val defaultListOrder = remember {
            listOf(
                ListPreferenceEntry(context.getString(R.string.score), RowOrder.SCORE),
                ListPreferenceEntry(context.getString(R.string.title), RowOrder.TITLE),
                ListPreferenceEntry(
                    context.getString(R.string.last_updated), RowOrder.LAST_UPDATED
                ),
                ListPreferenceEntry(context.getString(R.string.last_added), RowOrder.LAST_ADDED),
            )
        }

        ListPreferenceItem(
            value = listSettingsModel.rowOrder.value,
            title = stringResource(id = R.string.settings_default_list_order),
            entries = defaultListOrder
        ) {
            listSettingsModel.rowOrder.value = it!!
        }

        GroupPreferenceItem(title = stringResource(id = R.string.settings_split_completed_list_section_by_format)) {
            SwitchPreferenceItem(
                title = stringResource(id = R.string.settings_anime_list),
                checked = listSettingsModel.splitCompletedAnimeSectionByFormat.value
            ) {
                listSettingsModel.splitCompletedAnimeSectionByFormat.value = it
            }

            SwitchPreferenceItem(
                title = stringResource(id = R.string.settings_manga_list),
                checked = listSettingsModel.splitCompletedMangaSectionByFormat.value

            ) {
                listSettingsModel.splitCompletedMangaSectionByFormat.value = it
            }
        }


        if (listSettingsModel.showAdvancedScoring.value) {
            GroupPreferenceItem(title = stringResource(id = R.string.settings_advanced_scoring)) {
                SwitchPreferenceItem(
                    title = stringResource(id = R.string.enabled),
                    checked = listSettingsModel.advancedScoringEnabled.value
                ) {
                    listSettingsModel.advancedScoringEnabled.value = it
                }

                if (listSettingsModel.advancedScoringEnabled.value) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        value = listSettingsModel.advancedScoringText.value,
                        onValueChange = {
                            listSettingsModel.advancedScoringText.value = it
                        },
                        label = { Text(text = stringResource(id = R.string.advanced_scores)) },
                        trailingIcon = {
                            IconButton(onClick = {
                                listSettingsModel.advancedScoring.add(listSettingsModel.advancedScoringText.value)
                                listSettingsModel.advancedScoringText.value = ""
                            }) {
                                Icon(imageVector = AppIcons.IcPlus, contentDescription = null)
                            }
                        })

                    FlowRow(
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        listSettingsModel.advancedScoring.forEach { customList ->
                            ClearAssistChip(
                                modifier = Modifier.padding(horizontal = 2.dp),
                                text = customList,
                                onClear = {
                                    listSettingsModel.advancedScoring.remove(customList)
                                }) {

                            }
                        }
                    }
                }

            }
        }


        GroupPreferenceItem(title = stringResource(id = R.string.settings_custom_anime_lists)) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                value = listSettingsModel.animeCustomListText.value,
                onValueChange = {
                    listSettingsModel.animeCustomListText.value = it
                },
                label = { Text(text = stringResource(id = R.string.name)) },
                trailingIcon = {
                    IconButton(onClick = {
                        listSettingsModel.animeCustomLists.add(listSettingsModel.animeCustomListText.value)
                        listSettingsModel.animeCustomListText.value = ""
                    }) {
                        Icon(imageVector = AppIcons.IcPlus, contentDescription = null)
                    }
                })
            FlowRow(
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                listSettingsModel.animeCustomLists.forEach { customList ->
                    ClearAssistChip(
                        modifier = Modifier.padding(horizontal = 2.dp),
                        text = customList, onClear = {
                            listSettingsModel.animeCustomLists.remove(customList)
                        }) {

                    }
                }
            }
        }

        GroupPreferenceItem(title = stringResource(id = R.string.settings_custom_manga_lists)) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                value = listSettingsModel.mangaCustomListText.value,
                onValueChange = {
                    listSettingsModel.mangaCustomListText.value = it
                },
                label = { Text(text = stringResource(id = R.string.name)) },
                trailingIcon = {
                    IconButton(onClick = {
                        listSettingsModel.mangaCustomLists.add(listSettingsModel.mangaCustomListText.value)
                        listSettingsModel.mangaCustomListText.value = ""
                    }) {
                        Icon(imageVector = AppIcons.IcPlus, contentDescription = null)
                    }
                })

            FlowRow(
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                listSettingsModel.mangaCustomLists.forEach { customList ->
                    ClearAssistChip(
                        modifier = Modifier.padding(horizontal = 2.dp),
                        text = customList, onClear = {
                            listSettingsModel.mangaCustomLists.remove(customList)
                        }) {

                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun MediaListSettingsScreenPreview() {
    MaterialTheme(
        colorScheme = defaultTheme.lightColorScheme
    ) {
        Surface {
            MediaListSettingsScreenContent(
                MediaListOptionModel().toModel()
            )
        }
    }
}
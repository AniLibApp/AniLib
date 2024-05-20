package com.revolgenx.anilib.setting.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import anilib.i18n.R
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcPlus
import com.revolgenx.anilib.common.ui.icons.appicon.IcSave
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import com.revolgenx.anilib.setting.ui.component.GroupPreferenceItem
import com.revolgenx.anilib.setting.ui.component.ListPreferenceEntry
import com.revolgenx.anilib.setting.ui.component.ListPreferenceItem
import com.revolgenx.anilib.setting.ui.component.SwitchPreferenceItem

object MediaListSettingsScreen : AndroidScreen() {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val context = localContext()

        ScreenScaffold(
            title = stringResource(id = R.string.settings_lists),
            actions = {
                ActionMenu(
                    icon = AppIcons.IcSave
                ) {

                }
            },
            contentWindowInsets = horizontalBottomWindowInsets()
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {

                val scoringEntries = remember {
                    listOf(
                        ListPreferenceEntry(context.getString(R.string.settings_list_100_point), 0),
                        ListPreferenceEntry(
                            context.getString(R.string.settings_list_10_point_decimal),
                            1
                        ),
                        ListPreferenceEntry(context.getString(R.string.settings_list_10_point), 2),
                        ListPreferenceEntry(context.getString(R.string.settings_list_5_star), 3),
                        ListPreferenceEntry(context.getString(R.string.settings_list_3_star), 4),
                    )
                }
                ListPreferenceItem(
                    value = 0,
                    title = stringResource(id = R.string.settings_scoring_system),
                    entries = scoringEntries
                ) {

                }

                val defaultListOrder = remember {
                    listOf(
                        ListPreferenceEntry(context.getString(R.string.score), 0),
                        ListPreferenceEntry(context.getString(R.string.title), 1),
                        ListPreferenceEntry(context.getString(R.string.last_updated), 2),
                        ListPreferenceEntry(context.getString(R.string.last_added), 3),
                    )
                }

                ListPreferenceItem(
                    value = 0,
                    title = stringResource(id = R.string.settings_default_list_order),
                    entries = defaultListOrder
                ) {

                }

                GroupPreferenceItem(title = stringResource(id = R.string.settings_split_completed_list_section_by_format)) {
                    SwitchPreferenceItem(title = stringResource(id = R.string.settings_anime_list)) {

                    }

                    SwitchPreferenceItem(title = stringResource(id = R.string.settings_manga_list)) {

                    }
                }

                GroupPreferenceItem(title = stringResource(id = R.string.settings_custom_anime_lists)) {
                    TextField(
                        value = "",
                        onValueChange = {

                        },
                        trailingIcon = {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(imageVector = AppIcons.IcPlus, contentDescription = null)
                            }
                        }
                    )
                }

                GroupPreferenceItem(title = stringResource(id = R.string.settings_custom_manga_lists)) {
                    TextField(
                        value = "",
                        onValueChange = {

                        },
                        trailingIcon = {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(imageVector = AppIcons.IcPlus, contentDescription = null)
                            }
                        }
                    )
                }
            }
        }
    }

}

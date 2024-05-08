package com.revolgenx.anilib.setting.ui.screen

import android.content.Context
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import anilib.i18n.R
import com.materialkolor.dynamicColorScheme
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.color.ColorDialog
import com.maxkeppeler.sheets.color.models.ColorSelection
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.util.OnClickWithValue
import com.revolgenx.anilib.setting.ui.component.GroupPreferenceItem
import com.revolgenx.anilib.setting.ui.component.ListPreferenceEntry
import com.revolgenx.anilib.setting.ui.component.ListPreferenceItem
import com.revolgenx.anilib.setting.ui.component.TextPreferenceItem
import com.revolgenx.anilib.setting.ui.viewmodel.AppearanceSettingsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

object AppearanceSettingsScreen : PreferencesScreen() {
    override val titleRes: Int = I18nR.string.settings_appearance

    @Composable
    override fun PreferencesContent() {
        val viewModel: AppearanceSettingsViewModel = koinViewModel()
        val scope = rememberCoroutineScope()
        val themeDataStore = viewModel.themeDataStore
        val themeData = themeDataStore.collectAsState().value

        val systemInDarkTheme = isSystemInDarkTheme()
        val isDarkTheme = themeData.darkMode ?: systemInDarkTheme

        val context = localContext()
        val themeModeEntries = remember {
            getThemeModeEntries(context)
        }

        val themeEntries = remember {
            context.resources.getStringArray(com.revolgenx.anilib.R.array.theme_entries)
                .mapIndexed { index, s ->
                    ListPreferenceEntry(s, index)
                }
        }

        GroupPreferenceItem(title = context.getString(I18nR.string.theme)) {
            ListPreferenceItem(
                value = themeData.darkMode,
                title = stringResource(id = I18nR.string.settings_theme_dark_mode),
                entries = themeModeEntries
            ) { darkMode ->
                scope.launch {
                    viewModel.setTheme(
                        themeData.theme,
                        isDarkTheme = darkMode ?: systemInDarkTheme,
                        darkMode = darkMode
                    )
                }
            }

            ListPreferenceItem(
                value = themeData.theme,
                title = stringResource(id = I18nR.string.themes),
                entries = themeEntries
            ) { selectedTheme ->
                viewModel.setTheme(
                    selectedTheme!!,
                    isDarkTheme = isDarkTheme,
                    darkMode = themeData.darkMode
                )
            }
        }

        GroupPreferenceItem(title = stringResource(I18nR.string.color)) {
            ColorGroupContent(
                color = themeData.background,
                title = stringResource(id = R.string.surface_color)
            ) { selectedColor ->
                viewModel.setBackground(selectedColor)
            }

            ColorGroupContent(
                color = themeData.surfaceVariant,
                title = stringResource(id = R.string.surface_variant_color)
            ) { selectedColor ->
                viewModel.setSurfaceVariant(selectedColor)
            }

            ColorGroupContent(
                color = themeData.surfaceContainer,
                title = stringResource(id = R.string.surface_container_color)
            ) { selectedColor ->
                viewModel.setSurfaceContainer(selectedColor)
            }

            ColorGroupContent(
                color = themeData.primary,
                title = stringResource(id = R.string.primary_color)
            ) { selectedColor ->
                viewModel.setPrimary(selectedColor)
            }

            ColorGroupContent(
                color = themeData.primaryContainer,
                title = stringResource(id = R.string.primary_container_color)
            ) { selectedColor ->
                viewModel.setPrimaryContainer(selectedColor)
            }



        }


        GroupPreferenceItem(title = stringResource(I18nR.string.seeding)) {
            ColorGroupContent(
                color = themeData.primary,
                title = stringResource(id = R.string.seed_color)
            ) { selectedColor ->
                scope.launch {
                    themeDataStore.source.updateData {
                        it.copy(primary = selectedColor)
                    }
                }
            }

        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun ColorGroupContent(
        color: Int,
        title: String,
        onColorSelected: OnClickWithValue<Int>
    ) {
        val state = rememberUseCaseState()
        ColorDialog(
            state = state,
            selection = ColorSelection(
                onSelectColor = { selectedColor ->
                    onColorSelected(selectedColor)
                }
            ))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    state.show()
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                TextPreferenceItem(
                    modifier = Modifier.weight(1f),
                    title = title,
                    subtitle = Integer.toHexString(color)
                )
            }
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(40.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color(color))
            )

        }
    }


    private fun getThemeModeEntries(context: Context): List<ListPreferenceEntry<Boolean>> {
        return mutableListOf<ListPreferenceEntry<Boolean>>().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                add(
                    ListPreferenceEntry(
                        title = context.getString(I18nR.string.settings_theme_dark_follow_system),
                        value = null
                    )
                )
            }
            add(
                ListPreferenceEntry(
                    title = context.getString(I18nR.string.settings_theme_dark_off),
                    value = false
                )
            )
            add(
                ListPreferenceEntry(
                    title = context.getString(I18nR.string.settings_theme_dark_on),
                    value = true
                )
            )
        }
    }


}

package com.revolgenx.anilib.setting.ui.screen

import android.content.Context
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import anilib.i18n.R
import com.revolgenx.anilib.common.data.constant.ThemeModes
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ui.color.ColorPickerDialog
import com.revolgenx.anilib.common.ui.theme.primaryColors
import com.revolgenx.anilib.common.ui.theme.primaryContainerColors
import com.revolgenx.anilib.common.ui.theme.surfaceColors
import com.revolgenx.anilib.common.ui.theme.surfaceContainerColors
import com.revolgenx.anilib.common.ui.theme.surfaceContainerLowColors
import com.revolgenx.anilib.common.util.OnClickWithValue
import com.revolgenx.anilib.setting.ui.component.GroupPreferenceItem
import com.revolgenx.anilib.setting.ui.component.ListPreferenceEntry
import com.revolgenx.anilib.setting.ui.component.ListPreferenceItem
import com.revolgenx.anilib.setting.ui.component.PrefsHorizontalPadding
import com.revolgenx.anilib.setting.ui.component.PrefsVerticalPadding
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

        GroupPreferenceItem(title = context.getString(I18nR.string.display_scale)) {
            val displayScaleValue = viewModel.displayScale.collectAsState()
            val displayScale = remember(displayScaleValue.value) {
                mutableFloatStateOf(displayScaleValue.value!!)
            }
            Slider(
                modifier = Modifier.padding(
                    horizontal = PrefsHorizontalPadding,
                    vertical = PrefsVerticalPadding
                ),
                value = displayScale.floatValue,
                steps = 6,
                onValueChange = { range ->
                    displayScale.floatValue = range
                },
                valueRange = 0.8f..1f,
                onValueChangeFinished = {
                    viewModel.setDisplayScale(displayScale.floatValue)
                }
            )
        }


        val themeModeEntries = remember {
            getThemeModeEntries(context)
        }

        val themeEntries = remember {
            context.resources.getStringArray(com.revolgenx.anilib.R.array.theme_entries)
                .mapIndexed { index, s ->
                    ListPreferenceEntry(s, ThemeModes.entries[index])
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
                        darkMode = darkMode,
                        context = context
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
                    darkMode = themeData.darkMode,
                    context = context
                )
            }
        }

        val isCustomTheme = themeData.theme == ThemeModes.CUSTOM

        GroupPreferenceItem(title = stringResource(I18nR.string.color)) {
            ColorGroupContent(
                color = themeData.background,
                title = stringResource(id = R.string.surface_color),
                subtitle = stringResource(id = R.string.surface_color_desc),
                enabled = isCustomTheme,
                colorsInt = surfaceColors
            ) { selectedColor ->
                viewModel.setBackground(selectedColor, context)
            }


            // card
            ColorGroupContent(
                color = themeData.surfaceContainerLow,
                title = stringResource(id = R.string.surface_container_low_color),
                subtitle = stringResource(id = R.string.surface_container_low_color_desc),
                enabled = isCustomTheme,
                colorsInt = surfaceContainerLowColors
            ) { selectedColor ->
                viewModel.setSurfaceContainerLow(selectedColor)
            }

            // navigation bar
            ColorGroupContent(
                color = themeData.surfaceContainer,
                title = stringResource(id = R.string.surface_container_color),
                subtitle = stringResource(id = R.string.surface_container_color_desc),
                enabled = isCustomTheme,
                colorsInt = surfaceContainerColors
            ) { selectedColor ->
                viewModel.setSurfaceContainer(selectedColor)
            }

            ColorGroupContent(
                color = themeData.primary,
                title = stringResource(id = R.string.primary_color),
                subtitle = stringResource(id = R.string.primary_color_desc),
                enabled = isCustomTheme,
                colorsInt = primaryColors
            ) { selectedColor ->
                viewModel.setPrimary(selectedColor)
            }


            // floating action button
            ColorGroupContent(
                color = themeData.primaryContainer,
                title = stringResource(id = R.string.primary_container_color),
                subtitle = stringResource(id = R.string.primary_container_color_desc),
                enabled = isCustomTheme,
                colorsInt = primaryContainerColors
            ) { selectedColor ->
                viewModel.setPrimaryContainer(selectedColor)
            }

        }


        GroupPreferenceItem(title = stringResource(I18nR.string.seeding)) {
            ColorGroupContent(
                color = themeData.seedColor,
                title = stringResource(id = R.string.seed_color),
                subtitle = stringResource(id = R.string.surface_color_desc),
                enabled = isCustomTheme
            ) { selectedColor ->
                viewModel.seedColor(color = selectedColor, isDarkTheme = isDarkTheme)
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

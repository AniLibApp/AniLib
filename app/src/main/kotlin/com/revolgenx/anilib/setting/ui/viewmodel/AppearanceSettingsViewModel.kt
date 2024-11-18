package com.revolgenx.anilib.setting.ui.viewmodel

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.ViewModel
import com.materialkolor.Contrast
import com.materialkolor.dynamicColorScheme
import com.materialkolor.dynamiccolor.ContrastCurve
import com.materialkolor.dynamiccolor.DynamicColor
import com.materialkolor.dynamiccolor.MaterialDynamicColors
import com.materialkolor.ktx.getColor
import com.materialkolor.ktx.toHct
import com.materialkolor.palettes.TonalPalette
import com.materialkolor.scheme.SchemeTonalSpot
import com.revolgenx.anilib.common.data.constant.ThemeModes
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.data.store.theme.CustomThemeDataStore
import com.revolgenx.anilib.common.data.store.theme.ThemeDataStore
import com.revolgenx.anilib.common.data.store.theme.WidgetThemeDataStore
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.theme.beeDarkTheme
import com.revolgenx.anilib.common.ui.theme.beeTheme
import com.revolgenx.anilib.common.ui.theme.defaultDarkTheme
import com.revolgenx.anilib.common.ui.theme.defaultTheme
import com.revolgenx.anilib.common.ui.theme.greenAppleDarkTheme
import com.revolgenx.anilib.common.ui.theme.greenAppleTheme
import com.revolgenx.anilib.common.ui.theme.midnightDarkTheme
import com.revolgenx.anilib.common.ui.theme.midnightTheme
import com.revolgenx.anilib.common.ui.theme.strawberryDarkTheme
import com.revolgenx.anilib.common.ui.theme.strawberryTheme
import com.revolgenx.anilib.common.ui.theme.tealDarkTheme
import com.revolgenx.anilib.common.ui.theme.tealTheme
import com.revolgenx.anilib.common.util.isColorDark
import com.revolgenx.anilib.widget.ui.AiringScheduleWidget


class AppearanceSettingsViewModel(
    val themeDataStore: ThemeDataStore,
    private val customThemeDataStore: CustomThemeDataStore,
    private val widgetThemeDataStore: WidgetThemeDataStore,
    private val appPreferencesDataStore: AppPreferencesDataStore
) : ViewModel() {
    companion object {
        val colors = MaterialDynamicColors(false)
    }

    val displayScale = appPreferencesDataStore.displayScale

    fun setDisplayScale(scale: Float) {
        launch {
            displayScale.set(scale)
        }
    }

    fun setTheme(
        theme: ThemeModes,
        isDarkTheme: Boolean,
        darkMode: Boolean?,
        context: Context
    ) {
        launch {
            themeDataStore.source.updateData {
                when (theme) {
                    ThemeModes.DEFAULT -> {
                        if (isDarkTheme) defaultDarkTheme else defaultTheme
                    }
                    ThemeModes.MIDNIGHT -> {
                        if (isDarkTheme) midnightDarkTheme else midnightTheme
                    }
                    ThemeModes.BEE -> {
                        if (isDarkTheme) beeDarkTheme else beeTheme
                    }
                    ThemeModes.STRAWBERRY_DAIQUIRI -> {
                        if (isDarkTheme) strawberryDarkTheme else strawberryTheme
                    }
                    ThemeModes.TEAL-> {
                        if (isDarkTheme) tealDarkTheme else tealTheme
                    }
                    ThemeModes.GREEN_APPLE-> {
                        if (isDarkTheme) greenAppleDarkTheme else greenAppleTheme
                    }
                    ThemeModes.CUSTOM -> {
                        customThemeDataStore.get()
                    }
                }.copy(darkMode = darkMode)
            }

            updateWidgetTheme(context)
        }
    }


    fun setPrimary(color: Int) {
        launch {
            val onBg = foregroundColor(color)

            val customTheme = customThemeDataStore.source.updateData {
                it.copy(
                    theme = ThemeModes.CUSTOM,
                    primary = color,
                    onPrimary = onBg.toArgb()
                )
            }

            themeDataStore.source.updateData { customTheme }

        }
    }

    fun setPrimaryContainer(color: Int) {
        launch {
            val onBg = foregroundColor(color)
            val customTheme = customThemeDataStore.source.updateData {
                it.copy(
                    theme = ThemeModes.CUSTOM,
                    primaryContainer = color,
                    onPrimaryContainer = onBg.toArgb()
                )
            }
            themeDataStore.source.updateData { customTheme }

        }
    }

    fun setSurfaceContainer(color: Int) {
        launch {

            val customTheme = customThemeDataStore.source.updateData {
                it.copy(
                    theme = ThemeModes.CUSTOM,
                    surfaceContainer = color,
                )
            }

            themeDataStore.source.updateData { customTheme }

        }
    }

    fun setSurfaceContainerLow(color: Int) {
        launch {
            val onBg = foregroundColor(color)
            val customTheme = customThemeDataStore.source.updateData {
                it.copy(
                    theme = ThemeModes.CUSTOM,
                    surfaceContainerLow = color,
                    onSurfaceVariant = onBg.toArgb()
                )
            }

            themeDataStore.source.updateData { customTheme }

        }
    }

    fun setBackground(color: Int, context: Context) {
        launch {
            val onBg = foregroundColor(color)
            val customTheme = customThemeDataStore.source.updateData {
                it.copy(
                    theme = ThemeModes.CUSTOM,
                    background = color,
                    onBackground = onBg.toArgb(),
                )
            }

            themeDataStore.source.updateData { customTheme }
            updateWidgetTheme(context)
        }
    }

    fun seedColor(color: Int, isDarkTheme: Boolean) {
        launch {
            val colorScheme = dynamicColorScheme(seedColor = Color(color), isDark = isDarkTheme)


            val customTheme = customThemeDataStore.source.updateData {
                with(colorScheme) {
                    it.copy(
                        theme = ThemeModes.CUSTOM,
                        primary = primary.toArgb(),
                        onPrimary = onPrimary.toArgb(),
                        primaryContainer = primaryContainer.toArgb(),
                        onPrimaryContainer = onPrimaryContainer.toArgb(),
                        background = background.toArgb(),
                        onBackground = onBackground.toArgb(),
                        surfaceContainerLow = surfaceContainerLow.toArgb(),
                        surfaceContainer = surfaceContainer.toArgb(),
                        onSurfaceVariant = onSurfaceVariant.toArgb(),
                        seedColor = color
                    )
                }
            }
            themeDataStore.source.updateData { customTheme }

        }
    }

    private suspend fun updateWidgetTheme(context: Context){
        val widgetHaveSameBackground = appPreferencesDataStore.widgetBackgroundSameAsApp.get()!!
        if(widgetHaveSameBackground){
            val appTheme = themeDataStore.get()
            widgetThemeDataStore.source.updateData {
                it.copy(
                    background = appTheme.background,
                    onBackground = appTheme.onBackground,
                    onSurfaceVariant = Color(appTheme.onBackground).copy(alpha = 0.9f).toArgb()
                )
            }
            AiringScheduleWidget().updateAll(context = context)
        }
    }

    private fun  foregroundColor(color: Int): Color {
        val bg = Color(color)
        val bgHct = bg.toHct()
        val isDark = isColorDark(bg)
        val scheme = SchemeTonalSpot(bgHct, isDark = isDark, contrastLevel = Contrast.Default.value)

        return DynamicColor(
            name = "on_background",
            palette = { TonalPalette.fromHct(bgHct) },
            tone = { if (scheme.isDark) 90.0 else 10.0 },
            isBackground = false,
            background = {
                DynamicColor(
                    name = "background",
                    palette = { TonalPalette.fromHct(bgHct) },
                    tone = { if (scheme.isDark) 6.0 else 98.0 },
                    isBackground = true,
                    background = null,
                    secondBackground = null,
                    contrastCurve = null,
                    toneDeltaPair = null,
                )
            },
            secondBackground = null,
            contrastCurve = ContrastCurve(3.0, 3.0, 4.5, 7.0),
            toneDeltaPair = null,
        ).getColor(scheme)
    }

}
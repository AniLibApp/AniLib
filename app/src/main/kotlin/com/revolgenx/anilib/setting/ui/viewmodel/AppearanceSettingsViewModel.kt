package com.revolgenx.anilib.setting.ui.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import com.materialkolor.Contrast
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamiccolor.ContrastCurve
import com.materialkolor.dynamiccolor.DynamicColor
import com.materialkolor.dynamiccolor.MaterialDynamicColors
import com.materialkolor.ktx.getColor
import com.materialkolor.ktx.harmonize
import com.materialkolor.ktx.toDynamicScheme
import com.materialkolor.ktx.toHct
import com.materialkolor.palettes.TonalPalette
import com.materialkolor.scheme.SchemeTonalSpot
import com.revolgenx.anilib.common.data.store.theme.ThemeDataStore
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
import com.revolgenx.anilib.common.ui.theme.takoDarkTheme
import com.revolgenx.anilib.common.ui.theme.takoTheme
import com.revolgenx.anilib.common.ui.theme.tealDarkTheme
import com.revolgenx.anilib.common.ui.theme.tealTheme
import com.revolgenx.anilib.common.util.isColorDark


class AppearanceSettingsViewModel(
    val themeDataStore: ThemeDataStore
) : ViewModel() {
    companion object {
        val colors = MaterialDynamicColors(false)
    }


    fun setTheme(
        theme: Int,
        isDarkTheme: Boolean,
        darkMode: Boolean?
    ) {
        launch {
            themeDataStore.source.updateData {
                when (theme) {
                    0 -> {
                        if (isDarkTheme) defaultDarkTheme else defaultTheme
                    }

                    1 -> {
                        if (isDarkTheme) defaultDarkTheme else defaultTheme
                    }

                    2 -> {
                        if (isDarkTheme) midnightDarkTheme else midnightTheme
                    }

                    3 -> {
                        if (isDarkTheme) beeDarkTheme else beeTheme
                    }

                    4 -> {
                        if (isDarkTheme) strawberryDarkTheme else strawberryTheme
                    }

                    5 -> {
                        if (isDarkTheme) takoDarkTheme else takoTheme
                    }

                    6 -> {
                        if (isDarkTheme) tealDarkTheme else tealTheme
                    }

                    7 -> {
                        if (isDarkTheme) greenAppleDarkTheme else greenAppleTheme
                    }

                    else -> {
                        it
                    }
                }.copy(darkMode = darkMode)
            }
        }
    }


    fun setPrimary(color: Int) {
        launch {
            themeDataStore.source.updateData {
                val onBg = foreGroundColor(color)
                it.copy(
                    theme = 1,
                    primary = color,
                    onPrimary = onBg.toArgb()
                )
            }
        }
    }

    fun setPrimaryContainer(color: Int){
        launch {
            themeDataStore.source.updateData {
                val onBg = foreGroundColor(color)
                it.copy(
                    theme = 1,
                    primaryContainer = color,
                    onPrimaryContainer = onBg.toArgb()
                )
            }
        }
    }

    fun setSurfaceContainer(color: Int){
        launch {
            themeDataStore.source.updateData {
                it.copy(
                    theme = 1,
                    surfaceContainer = color,
                )
            }
        }
    }

    fun setSurfaceVariant(color: Int) {
        launch {
            themeDataStore.source.updateData {
                val onBg = foreGroundColor(color)
                it.copy(
                    theme = 1,
                    surfaceVariant = color,
                    onSurfaceVariant = onBg.toArgb()
                )
            }
        }
    }

    fun setBackground(color: Int) {
        launch {
            themeDataStore.source.updateData {
                val onBg = foreGroundColor(color)
                it.copy(
                    theme = 1,
                    background = color,
                    onBackground = onBg.toArgb(),
                )
            }
        }
    }

    private fun foreGroundColor(color: Int): Color {
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
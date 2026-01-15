package com.revolgenx.anilib.setting.ui.viewmodel

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.ViewModel
import com.materialkolor.Contrast
import com.materialkolor.dynamiccolor.ContrastCurve
import com.materialkolor.dynamiccolor.DynamicColor
import com.materialkolor.ktx.getColor
import com.materialkolor.ktx.toHct
import com.materialkolor.palettes.TonalPalette
import com.materialkolor.scheme.SchemeTonalSpot
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.data.store.theme.ThemeDataStore
import com.revolgenx.anilib.common.data.store.theme.WidgetThemeDataStore
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.util.isColorDark
import com.revolgenx.anilib.widget.ui.AiringScheduleWidget

class WidgetSettingsViewModel(
    val appPreferencesDataStore: AppPreferencesDataStore,
    val widgetThemeDataStore: WidgetThemeDataStore,
    themeDataStore: ThemeDataStore
) : ViewModel() {
    private val themeData = themeDataStore.get()

    fun resetBackground(context: Context) {
        launch {
            widgetThemeDataStore.source.updateData {
                themeData.copy()
            }
            updateWidget(context)
        }
    }

    fun setBackground(background: Int, context: Context) {
        launch {
            val onBg = foregroundColor(background)
            widgetThemeDataStore.source.updateData {
                it.copy(
                    background = background,
                    onBackground = onBg.toArgb(),
                    onSurfaceVariant = onBg.copy(alpha = 0.9f).toArgb()
                )
            }
            updateWidget(context)
        }
    }

    suspend fun updateWidget(context: Context) {
        AiringScheduleWidget().updateAll(context = context)
    }

    private fun foregroundColor(color: Int): Color {
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
            contrastCurve = { ContrastCurve(3.0, 3.0, 4.5, 7.0) },
            toneDeltaPair = null,
        ).getColor(scheme)
    }
}
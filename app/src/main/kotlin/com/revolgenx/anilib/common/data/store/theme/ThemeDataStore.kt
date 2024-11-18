package com.revolgenx.anilib.common.data.store.theme

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import com.revolgenx.anilib.common.data.constant.ThemeModes
import com.revolgenx.anilib.common.data.store.BaseDataStore
import com.revolgenx.anilib.common.data.store.BaseSerializer
import com.revolgenx.anilib.common.ui.theme.ElevationTokens
import com.revolgenx.anilib.common.util.colorAtElevation
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ThemeData(
    val darkMode: Boolean? = null, //system(null), dark(true), light(false)
    val theme: ThemeModes,
    val primary: Int,
    val onPrimary: Int,
    val primaryContainer: Int,
    val onPrimaryContainer: Int,
    val background: Int,
    val onBackground: Int,
    val surfaceContainer: Int,
    val surfaceContainerLow: Int,
    val onSurfaceVariant: Int,
    val seedColor: Int = primary
) {
    @Transient
    val lightColorScheme = lightColorScheme(
        primary = Color(primary),
        onPrimary = Color(onPrimary),
        primaryContainer = Color(primaryContainer),
        onPrimaryContainer = Color(onPrimaryContainer),
        background = Color(background),
        onBackground = Color(onBackground),
        surface = Color(background),
        onSurface = Color(onBackground),
        surfaceContainerLowest = Color(background),
        surfaceContainerLow = Color(surfaceContainerLow),
        surfaceContainer = Color(surfaceContainer),
        surfaceContainerHigh = Color(surfaceContainer),
        surfaceContainerHighest = Color(surfaceContainerLow),
        surfaceVariant = Color(surfaceContainerLow),
        onSurfaceVariant = Color(onSurfaceVariant),
        secondaryContainer = colorAtElevation(
            Color(surfaceContainer),
            Color(primary),
            ElevationTokens.Level3
        ),
        onSecondaryContainer = colorAtElevation(
            Color(onSurfaceVariant),
            Color(primary),
            ElevationTokens.Level3
        )
    )

    @Transient
    val darkColorScheme = darkColorScheme(
        primary = Color(primary),
        onPrimary = Color(onPrimary),
        primaryContainer = Color(primaryContainer),
        onPrimaryContainer = Color(onPrimaryContainer),
        background = Color(background),
        onBackground = Color(onBackground),
        surface = Color(background),
        onSurface = Color(onBackground),
        surfaceContainerLowest = Color(background),
        surfaceContainerLow = Color(surfaceContainerLow),
        surfaceContainer = Color(surfaceContainer),
        surfaceContainerHigh = Color(surfaceContainer),
        surfaceContainerHighest = Color(surfaceContainerLow),
        surfaceVariant = Color(surfaceContainerLow),
        onSurfaceVariant = Color(onSurfaceVariant),
        secondaryContainer = colorAtElevation(
            Color(surfaceContainer),
            Color(primary),
            ElevationTokens.Level3
        ),
        onSecondaryContainer = colorAtElevation(
            Color(onSurfaceVariant),
            Color(primary),
            ElevationTokens.Level3
        )
    )

    fun colorScheme(isDarkTheme: Boolean): ColorScheme {
        return if (isDarkTheme) darkColorScheme else lightColorScheme
    }

}

val Context.isDarkMode get() = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

class ThemeDataSerializer(
    val theme: ThemeData
) : BaseSerializer<ThemeData>(theme) {
    override fun serializer(): KSerializer<ThemeData> {
        return ThemeData.serializer()
    }
}

class ThemeDataStore(dataStore: DataStore<ThemeData>) : BaseDataStore<ThemeData>(dataStore)
class CustomThemeDataStore(dataStore: DataStore<ThemeData>) : BaseDataStore<ThemeData>(dataStore)
class WidgetThemeDataStore(dataStore: DataStore<ThemeData>) : BaseDataStore<ThemeData>(dataStore)
package com.revolgenx.anilib.common.data.store

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.datastore.preferences.core.booleanPreferencesKey

class ThemeDataStore(override val dataStore: PreferencesDataStore) : BasePreferenceDataStore() {
    companion object {
        val themeModeKey = booleanPreferencesKey("theme_mode_key")
        val themeModeSystem: Boolean? = null
        const val themeModeLight = false
//        const val themeModeDark = true
    }

    val themeMode = PreferenceDataModel(
        dataStore = dataStore,
        prefKey = themeModeKey,
        defaultValue = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) themeModeSystem else themeModeLight
    )

    @Composable
    fun isDark(): Boolean {
        val isInDarkTheme = isSystemInDarkTheme()
        val themeModeState = themeMode.collectAsNullableState()
        return themeModeState.value ?: isInDarkTheme
    }
}
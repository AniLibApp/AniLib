package com.revolgenx.anilib.common.data.store

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.revolgenx.anilib.common.data.model.PreferenceDataStoreModel

class ThemePreferencesDataStore(override val dataStore: PreferencesDataStore) :
    IPreferencesDataStore {
    companion object {
        val themeModeKey = booleanPreferencesKey("theme_mode_key")
        val themeModeSystem: Boolean? = null
        const val themeModeLight = false
//        const val themeModeDark = true
    }

    val themeMode = PreferenceDataStoreModel(
        dataStore = dataStore,
        prefKey = themeModeKey,
        defaultValue = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) themeModeSystem else themeModeLight
    )

    @Composable
    fun isDark(): Boolean {
        val isInDarkTheme = isSystemInDarkTheme()
        val themeModeState = themeMode.collectAsState()
        return themeModeState.value ?: isInDarkTheme
    }
}
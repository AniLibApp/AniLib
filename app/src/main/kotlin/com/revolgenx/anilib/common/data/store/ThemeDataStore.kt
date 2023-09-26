package com.revolgenx.anilib.common.data.store

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.revolgenx.anilib.setting.ui.model.PreferenceDataModel

class ThemeDataStore(private val dataStore: PreferencesDataStore) {
    companion object {
        val themeModeKey = booleanPreferencesKey("theme_mode_key")
    }

    // system if null, dark if true, light if false
    val themeMode = PreferenceDataModel(
        dataStore = dataStore,
        prefKey = themeModeKey,
        defaultValue = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) null else false
    )

    @Composable
    fun isDark(): Boolean {
        val isInDarkTheme = isSystemInDarkTheme()
        val themeModeState = themeMode.collectAsState()
        return themeModeState.value ?: isInDarkTheme
    }
}
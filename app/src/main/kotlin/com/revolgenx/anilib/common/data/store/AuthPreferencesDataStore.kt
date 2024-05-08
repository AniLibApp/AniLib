package com.revolgenx.anilib.common.data.store

import androidx.compose.runtime.Composable
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.auth0.android.jwt.JWT
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.media.ui.model.MediaTitleModel
import com.revolgenx.anilib.setting.data.store.MediaSettingsPreferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class AuthPreferencesDataStore(val dataStore: PreferencesDataStore) {
    companion object {
        val authTokenKey = stringPreferencesKey("auth_token_key")
        val userIdKey = intPreferencesKey("user_id_key")
    }

    val token = AppPreferencesDataStore(
        dataStore = dataStore,
        prefKey = authTokenKey
    )
    val userId = AppPreferencesDataStore(
        dataStore = dataStore,
        prefKey = userIdKey
    )

    val isLoggedIn = userId.data.map { it != null }

    @Composable
    fun isLoggedIn(): Boolean =userId.collectAsState().value != null

    fun continueIfLoggedIn(
        scope: CoroutineScope,
        callback: OnClick
    ) {
        scope.launch {
            userId.collect { id ->
                if (id != null) {
                    callback.invoke()
                }
            }
        }
    }

    suspend fun login(mToken: String) {
        val userId = JWT(mToken).subject!!.trim().toInt()
        dataStore.edit { pref ->
            pref[authTokenKey] = mToken
            pref[userIdKey] = userId
        }
    }

    suspend fun logout() {
        dataStore.edit { pref ->
            pref.remove(authTokenKey)
            pref.remove(userIdKey)
            pref.remove(MediaSettingsPreferencesDataStore.displayAdultContentKey)
            pref[MediaSettingsPreferencesDataStore.mediaTitleTypeKey] = MediaTitleModel.type_romaji
        }
    }
}



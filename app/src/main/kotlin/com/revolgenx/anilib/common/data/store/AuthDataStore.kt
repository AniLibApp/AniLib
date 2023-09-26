package com.revolgenx.anilib.common.data.store

import android.content.Context
import android.content.Intent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.auth0.android.jwt.JWT
import com.revolgenx.anilib.activity.MainActivity
import com.revolgenx.anilib.common.util.OnClick
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class AuthDataStore(private val dataStore: PreferencesDataStore) {
    companion object {
        val authTokenKey = stringPreferencesKey("auth_token_key")
        val userIdKey = intPreferencesKey("user_id_key")
    }

    val token = dataStore.data.map { it[authTokenKey] }
    val userId = dataStore.data.map { it[userIdKey] }
    val isLoggedIn = dataStore.data.map { it[userIdKey] != null }
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

    suspend fun login(token: String) {
        val userId = JWT(token).subject!!.trim().toInt()
        dataStore.edit { pref ->
            pref[authTokenKey] = token
            pref[userIdKey] = userId
        }
    }

    suspend fun logout() {
        dataStore.edit { pref ->
            pref.remove(authTokenKey)
            pref.remove(userIdKey)
        }
    }
}



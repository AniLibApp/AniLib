package com.revolgenx.anilib.common.data.store

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.auth0.android.jwt.JWT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


private val authTokenKey = stringPreferencesKey("auth_token_key")
private val userIdKey = intPreferencesKey("user_id_key")

private fun DataStore<Preferences>.token() = data.map { it[authTokenKey] }
private fun DataStore<Preferences>.userId() = data.map { it[userIdKey] }
private fun DataStore<Preferences>.isLoggedIn() = data.map { it[userIdKey] != null }

class AuthDataStore(private val preferenceDataStore: DataStore<Preferences>) {
    suspend fun login(token: String) {
        val userId = JWT(token).subject!!.trim().toInt()
        preferenceDataStore.edit { pref ->
            pref[authTokenKey] = token
            pref[userIdKey] = userId
        }
    }

    suspend fun logout() {
        preferenceDataStore.edit { pref ->
            pref.remove(authTokenKey)
            pref.remove(userIdKey)
        }
    }


    fun token() = preferenceDataStore.token()
    fun userId() = preferenceDataStore.userId()
}

@Composable
fun AuthDataStore.ShowIfLoggedIn(
    orElse: @Composable (() -> Unit) = {},
    content: @Composable (userId: Int) -> Unit
) {
    val userId = userId().collectAsState(initial = null).value
    if (userId != null) {
        content(userId)
    }else{
        orElse()
    }
}

@Composable
fun AuthDataStore.ShowIfLoggedInUser(userId: Int, content: @Composable () -> Unit) {
    val id = userId().collectAsState(initial = null).value
    if (id != null && id == userId) {
        content()
    }
}

fun AuthDataStore.continueIfLoggedIn(
    scope: CoroutineScope,
    callback: () -> Unit
) {
    scope.launch {
        val userId = this@continueIfLoggedIn.userId().first()
        if (userId != null) {
            callback.invoke()
        }
    }
}

fun Context.continueIfLoggedIn(
    scope: CoroutineScope,
    callback: () -> Unit
) {
    val dataStore = this.appPreferenceDataStore
    scope.launch {
        dataStore.token().first()?.let {
            callback.invoke()
        }
    }
}


@Composable
fun Context.isLoggedIn() =
    appPreferenceDataStore.isLoggedIn().collectAsState(initial = false)
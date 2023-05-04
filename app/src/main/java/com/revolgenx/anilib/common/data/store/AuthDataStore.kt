package com.revolgenx.anilib.common.data.store

import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.auth0.android.jwt.JWT
import com.revolgenx.anilib.activity.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


private val authTokenKey = stringPreferencesKey("auth_token_key")
private val userIdKey = intPreferencesKey("user_id_key")

fun AppPreferencesDataStore.token() = data.map { it[authTokenKey] }
fun AppPreferencesDataStore.userId() = data.map { it[userIdKey] }
fun AppPreferencesDataStore.isLoggedIn() = data.map { it[userIdKey] != null }

fun Context.userId() = appPreferencesDataStore.userId()
suspend fun Context.login(token: String) {
    val userId = JWT(token).subject!!.trim().toInt()
    appPreferencesDataStore.edit { pref ->
        pref[authTokenKey] = token
        pref[userIdKey] = userId
    }
}

suspend fun Context.logout() {
    appPreferencesDataStore.edit { pref ->
        pref.remove(authTokenKey)
        pref.remove(userIdKey)
    }
    startActivity(Intent(this, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    })
    if (this is MainActivity) {
        finish()
    }
}



fun AppPreferencesDataStore.continueIfLoggedIn(
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
    val dataStore = this.appPreferencesDataStore
    scope.launch {
        dataStore.token().first()?.let {
            callback.invoke()
        }
    }
}


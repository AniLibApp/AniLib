package com.revolgenx.anilib.preference

import android.content.Context
import androidx.preference.PreferenceManager
import com.revolgenx.anilib.model.TitleModel


fun Context.putBoolean(key: String, value: Boolean = false) =
    sharedPreference().edit().putBoolean(key, value).apply()

fun Context.getBoolean(key: String, def: Boolean = false) =
    sharedPreference().getBoolean(key, def)

fun Context.putString(key: String, value: String? = "") =
    sharedPreference().edit().putString(key, value).apply()

fun Context.getString(key: String, def: String = "") = sharedPreference().getString(key, def)

fun Context.putInt(key: String, value: Int = -1) =
    sharedPreference().edit().putInt(key, value).apply()

fun Context.getInt(key: String, def: Int) = sharedPreference().getInt(key, def)

fun Context.sharedPreference() = PreferenceManager.getDefaultSharedPreferences(this)


private const val titleKey = "title_key"

fun Context.titlePref() = getInt(titleKey, 0)
fun Context.titlePref(title: Int) = putInt(titleKey, title)
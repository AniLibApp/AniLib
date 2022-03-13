package com.revolgenx.anilib.common.preference

import com.pranavpandey.android.dynamic.preferences.DynamicPreferences

val dynamicPreferences get() = DynamicPreferences.getInstance()

fun load(key: String, default: String?) = dynamicPreferences.load(key, default)
fun save(key: String, value: String?) = dynamicPreferences.save(key, value)

fun load(key: String, default: Int) = dynamicPreferences.load(key, default)
fun save(key: String, value: Int?) = dynamicPreferences.save(key, value)

fun load(key: String, default: Boolean) = dynamicPreferences.load(key, default)
fun save(key: String, value: Boolean?) = dynamicPreferences.save(key, value)

fun loadStringSet(key: String, default: Set<String>?) = dynamicPreferences.loadStringSet(key, default)
fun saveStringSet(key: String, value: Set<String>?) = dynamicPreferences.save(key, value)


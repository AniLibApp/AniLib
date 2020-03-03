package com.revolgenx.anilib.preference

import android.content.Context

private const val loggedInKey = "logged_in_key"
private const val tokenKey = "token_key"
private const val titleKey = "title_key"


fun Context.loggedIn() = getBoolean(loggedInKey, false)
fun Context.loggedIn(logIn: Boolean) = putBoolean(loggedInKey, logIn)

fun Context.token() = getString(tokenKey, "")
fun Context.token(token: String) = putString(tokenKey, token)


fun Context.titlePref() = getInt(titleKey, 0)
fun Context.titlePref(title: Int) = putInt(titleKey, title)
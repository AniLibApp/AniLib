package com.revolgenx.anilib.preference

import android.content.Context


const val SEARCH_HISTORY_KEY = "search_history_key"

fun Context.saveSearchHistories(searches: String) {
    putString(SEARCH_HISTORY_KEY, if (searches.isEmpty()) null else searches)
}

fun Context.getAllSearchHistories() =
    sharedPreference().getString(SEARCH_HISTORY_KEY, null)?.split(",")?.toList() ?: emptyList()
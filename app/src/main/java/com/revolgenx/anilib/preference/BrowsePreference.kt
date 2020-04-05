package com.revolgenx.anilib.preference

import android.content.Context


const val SEARCH_HISTORY_KEY = "search_history_key"
const val BROWSE_FILTER_KEY = "browse_filter_key"

fun Context.saveSearchHistories(searches: String) {
    putString(SEARCH_HISTORY_KEY, if (searches.isEmpty()) null else searches)
}

fun Context.getAllSearchHistories() =
    sharedPreference().getString(SEARCH_HISTORY_KEY, null)?.split(",")?.toList() ?: emptyList()

fun Context.getBrowseFilterPreference() = getString(BROWSE_FILTER_KEY)
fun Context.setBrowseFilterPreference(filter: String) = putString(BROWSE_FILTER_KEY, filter)
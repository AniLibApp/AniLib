package com.revolgenx.anilib.preference

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


const val SEARCH_HISTORY_KEY = "search_history_key"
const val BROWSE_FILTER_KEY = "browse_filter_key"
const val TAG_LIST_KEY = "tag_list_key"
const val GENRE_LIST_KEY = "genre_list_key"
const val STREAM_LIST_KEY = "stream_list_key"

fun Context.saveSearchHistories(searches: String) {
    putString(SEARCH_HISTORY_KEY, if (searches.isEmpty()) null else searches)
}

fun Context.getAllSearchHistories() =
    sharedPreference().getString(SEARCH_HISTORY_KEY, null)?.split(",")?.toList() ?: emptyList()

fun Context.getBrowseFilterPreference() = getString(BROWSE_FILTER_KEY)
fun Context.setBrowseFilterPreference(filter: String) = putString(BROWSE_FILTER_KEY, filter)


fun getUserGenre(context: Context): List<String> {
    val typeToken = object : TypeToken<List<String>>() {}.type;
    return context.sharedPreference().getString(GENRE_LIST_KEY, "")!!.let {
        Gson().fromJson(it, typeToken) ?: emptyList()
    };
}

fun setUserGenre(context: Context, genre: List<String>) {
    Gson().toJson(genre).let {
        context.putString(GENRE_LIST_KEY, it)
    }
}

fun setUserStream(context: Context, streams: List<String>) {
    Gson().toJson(streams).let {
        context.putString(STREAM_LIST_KEY, it)
    }
}

fun getUserStream(context: Context): List<String> {
    val typeToken = object : TypeToken<List<String>>() {}.type;
    return context.sharedPreference().getString(STREAM_LIST_KEY, "")!!.let {
        Gson().fromJson(it, typeToken) ?: emptyList()
    }
}


fun getUserTag(context: Context): List<String> {
    val typeToken = object : TypeToken<List<String>>() {}.type;
    return context.sharedPreference().getString(TAG_LIST_KEY, "")!!.let {
        Gson().fromJson(it, typeToken) ?: emptyList()
    }
}

fun setUserTag(context: Context, genre: List<String>) {
    Gson().toJson(genre).let {
        context.putString(TAG_LIST_KEY, it)
    }
}


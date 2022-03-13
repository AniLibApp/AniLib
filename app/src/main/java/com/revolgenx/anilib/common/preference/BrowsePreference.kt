package com.revolgenx.anilib.common.preference

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


const val TAG_LIST_KEY = "tag_list_key"
const val GENRE_LIST_KEY = "genre_list_key"
const val STREAM_LIST_KEY = "stream_list_key"
const val EXCLUDED_TAG_KEY = "EXCLUDED_TAG_KEY"
const val EXCLUDED_GENRE_KEY = "EXCLUDED_GENRE_KEY"


fun getUserGenre(): List<String> {
    val typeToken = object : TypeToken<List<String>>() {}.type;
    return load(GENRE_LIST_KEY, "")!!.let {
        Gson().fromJson(it, typeToken) ?: emptyList()
    };
}

fun setUserGenre(genre: List<String>) {
    Gson().toJson(genre).let {
        save(GENRE_LIST_KEY, it)
    }
}

fun setUserStream(streams: List<String>) {
    Gson().toJson(streams).let {
        save(STREAM_LIST_KEY, it)
    }
}


fun getUserStream(): List<String> {
    val typeToken = object : TypeToken<List<String>>() {}.type;
    return load(STREAM_LIST_KEY, "")!!.let {
        Gson().fromJson(it, typeToken) ?: emptyList()
    }
}

fun getUserTag(): List<String> {
    val typeToken = object : TypeToken<List<String>>() {}.type;
    return load(TAG_LIST_KEY, "")!!.let {
        Gson().fromJson(it, typeToken) ?: emptyList()
    }
}

fun setUserTag(genre: List<String>) {
    save(TAG_LIST_KEY, Gson().toJson(genre))
}


fun setUserExcludedTags(tags: List<String>) {
    save(EXCLUDED_TAG_KEY, Gson().toJson(tags))
}

fun setUserExcludedGenre(genre: List<String>) {
    save(EXCLUDED_GENRE_KEY, Gson().toJson(genre))
}

fun getExcludedTags(): List<String> {
    val typeToken = object : TypeToken<List<String>>() {}.type;
    return load(EXCLUDED_TAG_KEY, "")!!.let {
        Gson().fromJson(it, typeToken) ?: emptyList()
    }
}

fun getExcludedGenre(): List<String> {
    val typeToken = object : TypeToken<List<String>>() {}.type;
    return load(EXCLUDED_GENRE_KEY, "")!!.let {
        Gson().fromJson(it, typeToken) ?: emptyList()
    }
}

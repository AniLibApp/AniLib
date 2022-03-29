package com.revolgenx.anilib.common.preference

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


const val TAG_LIST_KEY = "tag_list_key"
const val GENRE_LIST_KEY = "genre_list_key"
const val STREAM_LIST_KEY = "stream_list_key"
const val EXCLUDED_TAG_KEY = "EXCLUDED_TAG_KEY"
const val EXCLUDED_GENRE_KEY = "EXCLUDED_GENRE_KEY"
const val max_search_episode_key = "max_search_episode_key"
const val max_search_duration_key = "max_search_duration_key"
const val max_search_chapter_key = "max_search_chapter_key"
const val max_search_volume_key = "max_search_volume_key"
const val exclude_genre_everywhere_key = "exclude_genre_everywhere_key"
const val exclude_tags_everywhere_key = "exclude_tags_everywhere_key"


object SearchPreferenceDefault {
    const val SEARCH_MAX_EPISODE = 150
    const val SEARCH_MAX_DURATION = 170
    const val SEARCH_MAX_CHAPTER = 500
    const val SEARCH_MAX_VOLUME = 50
}

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

var maxEpisodesPref
    get() = load(max_search_episode_key, SearchPreferenceDefault.SEARCH_MAX_EPISODE)
    set(value) {
        save(max_search_episode_key, value)
    }

var maxDurationsPref
    get() = load(
        max_search_duration_key,
        SearchPreferenceDefault.SEARCH_MAX_DURATION
    )
    set(value) {
        save(max_search_duration_key, value)
    }

var maxChaptersPref
    get() = load(max_search_chapter_key, SearchPreferenceDefault.SEARCH_MAX_CHAPTER)
    set(value) {
        save(max_search_chapter_key, value)
    }

var maxVolumesPref
    get() = load(max_search_volume_key, SearchPreferenceDefault.SEARCH_MAX_VOLUME)
    set(value) {
        save(max_search_volume_key, value)
    }

val includeExcludedGenre get() = load(exclude_genre_everywhere_key, false)
val includeExcludedTags get() = load(exclude_tags_everywhere_key, false)


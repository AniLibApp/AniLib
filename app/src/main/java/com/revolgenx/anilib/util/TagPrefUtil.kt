package com.revolgenx.anilib.util

import android.content.Context
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.data.field.TagField
import com.revolgenx.anilib.common.data.meta.TagState
import com.revolgenx.anilib.common.preference.*

object TagPrefUtil {
    fun saveTagPref(tags: List<String>) {
        setUserTag(tags)
    }

    fun saveGenrePref(genre: List<String>) {
        setUserGenre(genre)
    }

    fun saveStreamingOnPref(streams: List<String>) {
        setUserStream(streams)
    }


    fun saveExcludedTags(tags: List<String>){
        setUserExcludedTags(tags)
    }

    fun saveExcludedGenre(tags: List<String>){
        setUserExcludedGenre(tags)
    }

    fun reloadTagPref(context: Context): List<TagField> {
        val tags = context.resources.getStringArray(R.array.media_tags).toList()

        saveTagPref(
            tags
        )
        return tags.map { TagField(it, TagState.EMPTY) }
    }


    fun getUserPrefTags(): List<String> = getUserTag()
    fun getUserPrefGenres() = getUserGenre()
    fun getUserPrefStreamingOn() = getUserStream()

    fun getExcludedPrefTags(): List<String> = getExcludedTags()
    fun getExcludedPrefGenre() = getExcludedGenre()


    fun reloadGenrePref(context: Context): List<TagField> {
        val tags = context.resources.getStringArray(R.array.media_genre).toList()
        saveGenrePref(tags)
        return tags.map { TagField(it, TagState.EMPTY) }
    }

    fun reloadStreamingPref(context: Context): List<TagField> {
        val tags = context.resources.getStringArray(R.array.streaming_on).toList()

        saveStreamingOnPref(
            tags
        )

        return tags.map { TagField(it, TagState.EMPTY) }
    }

    fun invalidateAll() {
        storeSeasonTag(null)
        storeSeasonGenre(null)
    }
}
package com.revolgenx.anilib.util

import android.content.Context
import com.revolgenx.anilib.R
import com.revolgenx.anilib.data.field.TagField
import com.revolgenx.anilib.data.field.TagState
import com.revolgenx.anilib.common.preference.*

object TagPrefUtil {
    fun saveTagPref(context: Context, tags: List<String>) {
        setUserTag(context, tags)
    }

    fun saveGenrePref(context: Context, genre: List<String>) {
        setUserGenre(context, genre)
    }

    fun saveStreamingOnPref(context: Context, streams: List<String>) {
        setUserStream(context, streams)
    }


    fun saveExcludedTags(context: Context, tags:List<String>){
        setUserExcludedTags(context, tags)
    }

    fun saveExcludedGenre(context: Context, tags:List<String>){
        setUserExcludedGenre(context, tags)
    }

    fun reloadTagPref(context: Context): List<TagField> {
        val tags = context.resources.getStringArray(R.array.media_tags).toList()

        saveTagPref(
            context,
            tags
        )
        return tags.map { TagField(it, TagState.EMPTY) }
    }


    fun getUserPrefTags(context: Context): List<String> = getUserTag(context)
    fun getUserPrefGenres(context: Context) = getUserGenre(context)
    fun getUserPrefStreamingOn(context: Context) = getUserStream(context)

    fun getExcludedPrefTags(context: Context): List<String> = getExcludedTags(context)
    fun getExcludedPrefGenre(context: Context) = getExcludedGenre(context)


    fun reloadGenrePref(context: Context): List<TagField> {
        val tags = context.resources.getStringArray(R.array.media_genre).toList()
        saveGenrePref(context, tags)
        return tags.map { TagField(it, TagState.EMPTY) }
    }

    fun reloadStreamingPref(context: Context): List<TagField> {
        val tags = context.resources.getStringArray(R.array.streaming_on).toList()

        saveStreamingOnPref(
            context,
            tags
        )

        return tags.map { TagField(it, TagState.EMPTY) }
    }

    fun invalidateAll(context: Context) {
        storeSeasonTag(context, null)
        storeSeasonGenre(context, null)
    }
}
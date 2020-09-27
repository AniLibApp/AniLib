package com.revolgenx.anilib.util

import android.content.Context
import com.revolgenx.anilib.R
import com.revolgenx.anilib.field.TagField
import com.revolgenx.anilib.field.TagState
import com.revolgenx.anilib.field.home.SeasonField
import com.revolgenx.anilib.model.search.filter.MediaSearchFilterModel
import com.revolgenx.anilib.preference.*
import com.revolgenx.anilib.util.BrowseFilterDataProvider.getBrowseFilterData
import com.revolgenx.anilib.util.BrowseFilterDataProvider.setBrowseFilterData

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
        getBrowseFilterData(context).takeIf { it is MediaSearchFilterModel }?.let {
            (it as MediaSearchFilterModel).let { f ->
                f.tags = emptyList()
                f.genre = emptyList()
                f.streamingOn = emptyList()
            }
            setBrowseFilterData(context, it)
        }
        storeSeasonTag(context, null)
        storeSeasonGenre(context, null)
    }
}
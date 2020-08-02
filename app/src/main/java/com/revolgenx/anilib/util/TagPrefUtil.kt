package com.revolgenx.anilib.util

import android.content.Context
import com.revolgenx.anilib.R
import com.revolgenx.anilib.field.TagField
import com.revolgenx.anilib.model.search.filter.MediaBrowseFilterModel
import com.revolgenx.anilib.preference.setUserGenre
import com.revolgenx.anilib.preference.setUserStream
import com.revolgenx.anilib.preference.setUserTag
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
        return tags.map { TagField(it, false) }
    }

    fun reloadGenrePref(context: Context): List<TagField> {
        val tags = context.resources.getStringArray(R.array.media_genre).toList()

        saveGenrePref(
            context,
            tags
        )

        return tags.map { TagField(it, false) }
    }

    fun reloadStreamingPref(context: Context): List<TagField> {
        val tags = context.resources.getStringArray(R.array.streaming_on).toList()

        saveStreamingOnPref(
            context,
            tags
        )

        return tags.map { TagField(it, false) }
    }

    fun invalidateAll(context: Context) {
        getBrowseFilterData(context).takeIf { it is MediaBrowseFilterModel }?.let {
            (it as MediaBrowseFilterModel).let { f ->
                f.tags = emptyList()
                f.genre = emptyList()
                f.streamingOn = emptyList()
            }
            setBrowseFilterData(context, it)
        }
    }
}